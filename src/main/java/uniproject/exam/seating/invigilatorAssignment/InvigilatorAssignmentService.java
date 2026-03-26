package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniproject.exam.seating.exam.Exam;
import uniproject.exam.seating.exam.examRepository;
import uniproject.exam.seating.invigilator.invigilator;
import uniproject.exam.seating.invigilator.invigilatorRepository;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.room.roomRepository;
import uniproject.exam.seating.seating.Seating;
import uniproject.exam.seating.seating.seatingRepository;

import java.util.*;

@Service
public class InvigilatorAssignmentService {

    private final InvigilatorAssignmentRepository assignmentRepo;
    private final invigilatorRepository invigilatorRepo;
    private final examRepository examRepo;
    private final seatingRepository seatingRepo;
    private final roomRepository roomRepo;

    public InvigilatorAssignmentService(InvigilatorAssignmentRepository assignmentRepo,
                                        invigilatorRepository invigilatorRepo,
                                        examRepository examRepo,
                                        seatingRepository seatingRepo,
                                        roomRepository roomRepo) {
        this.assignmentRepo = assignmentRepo;
        this.invigilatorRepo = invigilatorRepo;
        this.examRepo = examRepo;
        this.seatingRepo = seatingRepo;
        this.roomRepo = roomRepo;
    }

    public List<InvigilatorAssignment> getAllAssignment() {
        return assignmentRepo.findAll();
    }

    @Transactional
    public void generateAssignment(Integer examId) {
        // 1. Fetch the Exam Session
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found!"));

        // 2. Clear previous assignments for this specific exam (Allows safe re-generation)
        assignmentRepo.deleteByExam_ExamId(examId);
        assignmentRepo.flush();

        // 3. Fetch all active Rooms and shuffle all available Invigilators
        List<Room> allRooms = roomRepo.findAll();
        List<invigilator> availableInvigilators = new ArrayList<>(invigilatorRepo.findAll());
        Collections.shuffle(availableInvigilators); // Ensures fair, random distribution

        List<InvigilatorAssignment> newAssignments = new ArrayList<>();

        // 4. Loop through every room to assign staff
        for (Room room : allRooms) {
            // Get how many invigilators this room needs (from your updated Room entity)
            int requiredCapacity = room.getNumOfInvigilators() != null ? room.getNumOfInvigilators() : 0;
            if (requiredCapacity == 0) continue;

            // Find all unique student majors currently seated in this room
            List<Seating> roomSeats = seatingRepo.findByRoom_RoomId(room.getRoomId());
            if (roomSeats.isEmpty()) continue; // Skip empty rooms

            Set<String> studentMajorsInRoom = new HashSet<>();
            for (Seating seat : roomSeats) {
                if (seat.getStudent() != null) {
                    studentMajorsInRoom.add(seat.getStudent().getMajorId());
                }
            }

            List<invigilator> assignedToThisRoom = new ArrayList<>();

            // Step 1: Assign 1 CHIEF
            assignSpecificRank(availableInvigilators, assignedToThisRoom, studentMajorsInRoom,
                    invigilator.invigilatorRank.CHIEF, requiredCapacity);

            // Step 2: Assign 1 SENIOR
            assignSpecificRank(availableInvigilators, assignedToThisRoom, studentMajorsInRoom,
                    invigilator.invigilatorRank.SENIOR, requiredCapacity);

            // Step 3: Assign 1 ASSISTANT
            assignSpecificRank(availableInvigilators, assignedToThisRoom, studentMajorsInRoom,
                    invigilator.invigilatorRank.ASSISTANT, requiredCapacity);

            // Step 4: Fill remaining seats with anyone valid (Fallback if capacity > 3)
            Iterator<invigilator> it = availableInvigilators.iterator();
            while (it.hasNext() && assignedToThisRoom.size() < requiredCapacity) {
                invigilator inv = it.next();
                if (canAssign(inv, studentMajorsInRoom, assignedToThisRoom)) {
                    assignedToThisRoom.add(inv);
                    it.remove();
                }
            }

            // Map the successful assignments to our Entity
            for (invigilator inv : assignedToThisRoom) {
                newAssignments.add(new InvigilatorAssignment(exam, room, inv));
            }
        }

        // 5. Bulk save all assignments to the database
        assignmentRepo.saveAll(newAssignments);
    }

    // --- HELPER METHODS FOR THE ALGORITHM ---

    private void assignSpecificRank(List<invigilator> availablePool, List<invigilator> roomAssigned,
                                    Set<String> studentMajors, invigilator.invigilatorRank targetRank,
                                    int roomCapacity) {
        Iterator<invigilator> it = availablePool.iterator();
        while (it.hasNext() && roomAssigned.size() < roomCapacity) {
            invigilator inv = it.next();
            // Using your updated InvigilatorRank enum
            if (inv.getInvigilatorRank() == targetRank && canAssign(inv, studentMajors, roomAssigned)) {
                roomAssigned.add(inv);
                it.remove(); // Remove from the available pool so they aren't double-booked during this Exam
                break; // Only assign ONE of this specific rank in this step
            }
        }
    }

    private boolean canAssign(invigilator inv, Set<String> studentMajors, List<invigilator> assigned) {
        // Rule 1: Anti-Cheating (Invigilator's department cannot match any student's major)
        if (studentMajors.contains(inv.getDepartment())) {
            return false;
        }

        // Rule 2: Chain of Command (Only one CHIEF allowed per room)
        if (inv.getInvigilatorRank() == invigilator.invigilatorRank.CHIEF) {
            for (invigilator i : assigned) {
                if (i.getInvigilatorRank() == invigilator.invigilatorRank.CHIEF) {
                    return false;
                }
            }
        }

        return true;
    }
}