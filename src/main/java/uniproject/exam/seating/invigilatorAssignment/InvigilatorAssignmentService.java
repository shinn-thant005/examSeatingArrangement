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
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found!"));

        assignmentRepo.deleteByExam_ExamId(examId);
        assignmentRepo.flush();

        List<Room> allRooms = roomRepo.findAll();
        List<invigilator> availableInvigilators = new ArrayList<>(invigilatorRepo.findAll());
        Collections.shuffle(availableInvigilators);

        List<InvigilatorAssignment> newAssignments = new ArrayList<>();
        String targetMajor = exam.getTargetMajor();

        for (Room room : allRooms) {
            int requiredCapacity = room.getNumOfInvigilators() != null ? room.getNumOfInvigilators() : 0;
            if (requiredCapacity == 0) continue;

            List<Seating> roomSeats = seatingRepo.findByRoom_RoomId(room.getRoomId());
            if (roomSeats.isEmpty()) continue;

            // --- THE MIXED ROOM FIX ---
            boolean isExamHappeningInRoom = false;
            Set<String> allStudentMajorsInRoom = new HashSet<>();

            for (Seating seat : roomSeats) {
                if (seat.getStudent() != null) {
                    String stuMajor = seat.getStudent().getMajorId();

                    // 1. Collect EVERY major currently sitting in this room (The Shield)
                    allStudentMajorsInRoom.add(stuMajor);

                    // 2. Check if the exam we are scheduling is actually happening here (The Trigger)
                    if (targetMajor.equals(stuMajor)) {
                        isExamHappeningInRoom = true;
                    }
                }
            }

            // If none of the students in this room are taking this exam, skip it.
            if (!isExamHappeningInRoom) continue;

            List<invigilator> assignedToThisRoom = new ArrayList<>();

            // We pass the Set of ALL majors in the room to the helper methods now!
            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    invigilator.invigilatorRank.CHIEF, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    invigilator.invigilatorRank.SENIOR, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    invigilator.invigilatorRank.ASSISTANT, requiredCapacity);

            Iterator<invigilator> it = availableInvigilators.iterator();
            while (it.hasNext() && assignedToThisRoom.size() < requiredCapacity) {
                invigilator inv = it.next();
                if (canAssign(inv, allStudentMajorsInRoom, assignedToThisRoom)) {
                    assignedToThisRoom.add(inv);
                    it.remove();
                }
            }

            for (invigilator inv : assignedToThisRoom) {
                newAssignments.add(new InvigilatorAssignment(exam, room, inv));
            }
        }

        assignmentRepo.saveAll(newAssignments);
    }

    // --- HELPER METHODS ---

    private void assignSpecificRank(List<invigilator> availablePool, List<invigilator> roomAssigned,
                                    Set<String> studentMajorsInRoom, invigilator.invigilatorRank targetRank,
                                    int roomCapacity) {
        Iterator<invigilator> it = availablePool.iterator();
        while (it.hasNext() && roomAssigned.size() < roomCapacity) {
            invigilator inv = it.next();
            if (inv.getRank() == targetRank && canAssign(inv, studentMajorsInRoom, roomAssigned)) {
                roomAssigned.add(inv);
                it.remove();
                break;
            }
        }
    }

    private boolean canAssign(invigilator inv, Set<String> studentMajorsInRoom, List<invigilator> assigned) {
        // Rule 1: THE IRONCLAD SHIELD (If the invigilator's department matches ANY student in the room, BLOCK THEM)
        if (studentMajorsInRoom.contains(inv.getDepartment())) {
            return false;
        }

        // Rule 2: Chain of Command
        if (inv.getRank() == invigilator.invigilatorRank.CHIEF) {
            for (invigilator i : assigned) {
                if (i.getRank() == invigilator.invigilatorRank.CHIEF) {
                    return false;
                }
            }
        }

        return true;
    }

    public void deleteAssignmentById(Integer AssignmentId) {
        assignmentRepo.deleteById(AssignmentId);
    }

    public void deleteAllAssignmentByExam(Integer examId) {
        assignmentRepo.deleteAllByExam_ExamId(examId);
    }

    @Transactional
    public void updateAssignment(Integer assignmentId, String roomName, String invigilatorName) {
        InvigilatorAssignment targetAssignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment record not found with ID: " + assignmentId));

        Room newRoom = roomRepo.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("Room not found with Room Name: " + roomName));

        invigilator newInvigilator = invigilatorRepo.findByInvigilatorName(invigilatorName)
                .orElseThrow(() -> new RuntimeException("Invigilator not found with Invigilator Name: " + invigilatorName));


        targetAssignment.setRoom(newRoom);
        targetAssignment.setInvigilator(newInvigilator);
        assignmentRepo.save(targetAssignment);
    }


}