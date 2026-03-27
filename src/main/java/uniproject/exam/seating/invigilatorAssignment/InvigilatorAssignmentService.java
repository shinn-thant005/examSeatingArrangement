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

        // 2. Clear previous assignments
        assignmentRepo.deleteByExam_ExamId(examId);
        assignmentRepo.flush();

        // 3. Fetch Rooms and Shuffle Invigilators
        List<Room> allRooms = roomRepo.findAll();
        List<invigilator> availableInvigilators = new ArrayList<>(invigilatorRepo.findAll());
        Collections.shuffle(availableInvigilators);

        List<InvigilatorAssignment> newAssignments = new ArrayList<>();
        String targetMajor = exam.getTargetMajor(); // The major taking the exam

        // 4. Loop through every room
        for (Room room : allRooms) {
            int requiredCapacity = room.getNumOfInvigilators() != null ? room.getNumOfInvigilators() : 0;
            if (requiredCapacity == 0) continue;

            List<Seating> roomSeats = seatingRepo.findByRoom_RoomId(room.getRoomId());
            if (roomSeats.isEmpty()) continue;

            // Check if this specific exam is actually happening in this room
            boolean isExamHappeningInRoom = false;
            for (Seating seat : roomSeats) {
                if (seat.getStudent() != null && targetMajor.equals(seat.getStudent().getMajorId())) {
                    isExamHappeningInRoom = true;
                    break; // Found one student taking this exam, so we need staff here
                }
            }

            // Skip if no students are taking this exam in this room
            if (!isExamHappeningInRoom) continue;

            List<invigilator> assignedToThisRoom = new ArrayList<>();

            // Step 1-3: Assign Ranks. We pass targetMajor so it can block matching invigilators!
            assignSpecificRank(availableInvigilators, assignedToThisRoom, targetMajor,
                    invigilator.invigilatorRank.CHIEF, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, targetMajor,
                    invigilator.invigilatorRank.SENIOR, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, targetMajor,
                    invigilator.invigilatorRank.ASSISTANT, requiredCapacity);

            // Step 4: Fallback loop
            Iterator<invigilator> it = availableInvigilators.iterator();
            while (it.hasNext() && assignedToThisRoom.size() < requiredCapacity) {
                invigilator inv = it.next();
                if (canAssign(inv, targetMajor, assignedToThisRoom)) {
                    assignedToThisRoom.add(inv);
                    it.remove();
                }
            }

            // Map and Save
            for (invigilator inv : assignedToThisRoom) {
                newAssignments.add(new InvigilatorAssignment(exam, room, inv));
            }
        }

        assignmentRepo.saveAll(newAssignments);
    }

    // --- HELPER METHODS ---

    private void assignSpecificRank(List<invigilator> availablePool, List<invigilator> roomAssigned,
                                    String targetMajor, invigilator.invigilatorRank targetRank,
                                    int roomCapacity) {
        Iterator<invigilator> it = availablePool.iterator();
        while (it.hasNext() && roomAssigned.size() < roomCapacity) {
            invigilator inv = it.next();
            if (inv.getRank() == targetRank && canAssign(inv, targetMajor, roomAssigned)) {
                roomAssigned.add(inv);
                it.remove();
                break;
            }
        }
    }

    private boolean canAssign(invigilator inv, String targetMajor, List<invigilator> assigned) {
        // Rule 1: THE ANTI-CHEATING FIX (Invigilator's department MUST NOT match the Exam's major)
        if (targetMajor.equals(inv.getDepartment())) {
            return false;
        }

        // Rule 2: Chain of Command (Only one CHIEF allowed per room)
        if (inv.getRank() == invigilator.invigilatorRank.CHIEF) {
            for (invigilator i : assigned) {
                if (i.getRank() == invigilator.invigilatorRank.CHIEF) {
                    return false;
                }
            }
        }

        return true;
    }
}