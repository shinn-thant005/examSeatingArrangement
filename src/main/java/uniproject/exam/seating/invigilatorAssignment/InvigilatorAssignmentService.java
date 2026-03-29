package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uniproject.exam.seating.exam.Exam;
import uniproject.exam.seating.exam.ExamRepository;
import uniproject.exam.seating.invigilator.Invigilator;
import uniproject.exam.seating.invigilator.InvigilatorRepository;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.room.RoomRepository;
import uniproject.exam.seating.seating.Seating;
import uniproject.exam.seating.seating.SeatingRepository;
import uniproject.exam.seating.seating.SeatingService;

import java.util.stream.Collectors;


import java.util.*;

@Service
public class InvigilatorAssignmentService {

    private final InvigilatorAssignmentRepository assignmentRepo;
    private final InvigilatorRepository invigilatorRepo;
    private final ExamRepository examRepo;
    private final SeatingRepository seatingRepo;
    private final RoomRepository roomRepo;
    private final SeatingService seatingService;

    public InvigilatorAssignmentService(InvigilatorAssignmentRepository assignmentRepo,
                                        InvigilatorRepository invigilatorRepo,
                                        ExamRepository examRepo,
                                        SeatingRepository seatingRepo,
                                        RoomRepository roomRepo,
                                        SeatingService seatingService) {
        this.assignmentRepo = assignmentRepo;
        this.invigilatorRepo = invigilatorRepo;
        this.examRepo = examRepo;
        this.seatingRepo = seatingRepo;
        this.roomRepo = roomRepo;
        this.seatingService = seatingService;
    }

    public List<InvigilatorAssignment> getAllAssignment() {
        return assignmentRepo.findAll();
    }

    @Transactional
    public void generateAssignment(Integer examId) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found!"));

        // Clear existing assignments for THIS specific exam to avoid duplicates
        assignmentRepo.deleteAllByExam_ExamId(examId);
        assignmentRepo.flush();

        List<Room> allRooms = roomRepo.findAll();

        // --- NEW EFFICIENCY FIX: Fetch all seatings ONCE and group in memory ---
        // This prevents executing a database query inside the loop for every single room
        List<Seating> allSeatings = seatingRepo.findAll();
        Map<Integer, List<Seating>> seatsByRoom = allSeatings.stream()
                .collect(Collectors.groupingBy(s -> s.getRoom().getRoomId()));
        // -----------------------------------------------------------------------

        // --- DOUBLE-BOOKING PREVENTION LOGIC ---
        // 1. Get ALL invigilators in the system
        List<Invigilator> allInvigilators = invigilatorRepo.findAll();

        // 2. Ask the database: Who is already busy at this exact Date and Time?
        List<Invigilator> busyInvigilators = assignmentRepo.findBusyInvigilators(exam.getExamDate(), exam.getExamTime());

        // 3. Create the available pool by removing the busy ones
        List<Invigilator> availableInvigilators = new ArrayList<>(allInvigilators);
        availableInvigilators.removeAll(busyInvigilators);

        // Shuffle the remaining free invigilators
        Collections.shuffle(availableInvigilators);
        // ------------------------------------------

        List<InvigilatorAssignment> newAssignments = new ArrayList<>();
        String targetMajor = exam.getTargetMajor();

        for (Room room : allRooms) {
            int requiredCapacity = room.getNumOfInvigilators() != null ? room.getNumOfInvigilators() : 0;
            if (requiredCapacity == 0) continue;

            // EFFICIENCY FIX: Look up the room's seats from our memory map (Zero DB calls here!)
            List<Seating> roomSeats = seatsByRoom.getOrDefault(room.getRoomId(), new ArrayList<>());
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

            List<Invigilator> assignedToThisRoom = new ArrayList<>();

            // Pass the Set of ALL majors in the room to the helper methods
            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    Invigilator.invigilatorRank.CHIEF, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    Invigilator.invigilatorRank.SENIOR, requiredCapacity);

            assignSpecificRank(availableInvigilators, assignedToThisRoom, allStudentMajorsInRoom,
                    Invigilator.invigilatorRank.ASSISTANT, requiredCapacity);

            Iterator<Invigilator> it = availableInvigilators.iterator();
            while (it.hasNext() && assignedToThisRoom.size() < requiredCapacity) {
                Invigilator inv = it.next();
                if (canAssign(inv, allStudentMajorsInRoom, assignedToThisRoom)) {
                    assignedToThisRoom.add(inv);
                    it.remove();
                }
            }

            // Optional future safeguard: If assignedToThisRoom.size() < requiredCapacity,
            // you could log a warning here that you ran out of available invigilators!

            for (Invigilator inv : assignedToThisRoom) {
                newAssignments.add(new InvigilatorAssignment(exam, room, inv));
            }
        }

        // Save all assignments in one large batch at the very end
        assignmentRepo.saveAll(newAssignments);
    }

    // --- HELPER METHODS ---

    private void assignSpecificRank(List<Invigilator> availablePool, List<Invigilator> roomAssigned,
                                    Set<String> studentMajorsInRoom, Invigilator.invigilatorRank targetRank,
                                    int roomCapacity) {
        Iterator<Invigilator> it = availablePool.iterator();
        while (it.hasNext() && roomAssigned.size() < roomCapacity) {
            Invigilator inv = it.next();
            if (inv.getRank() == targetRank && canAssign(inv, studentMajorsInRoom, roomAssigned)) {
                roomAssigned.add(inv);
                it.remove();
                break;
            }
        }
    }

    private boolean canAssign(Invigilator inv, Set<String> studentMajorsInRoom, List<Invigilator> assigned) {
        // Rule 1: THE IRONCLAD SHIELD (If the invigilator's department matches ANY student in the room, BLOCK THEM)
        if (studentMajorsInRoom.contains(inv.getDepartment())) {
            return false;
        }

        // Rule 2: Chain of Command
        if (inv.getRank() == Invigilator.invigilatorRank.CHIEF) {
            for (Invigilator i : assigned) {
                if (i.getRank() == Invigilator.invigilatorRank.CHIEF) {
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

        Invigilator newInvigilator = invigilatorRepo.findByInvigilatorName(invigilatorName)
                .orElseThrow(() -> new RuntimeException("Invigilator not found with Invigilator Name: " + invigilatorName));


        Exam currentExam = targetAssignment.getExam();

        // 2. Ask the database: Is this invigilator already booked for this Date and Time (somewhere else)?
        boolean isDoubleBooked = assignmentRepo.existsByInvigilatorAndExam_ExamDateAndExam_ExamTimeAndAssignmentIdNot(
                newInvigilator,
                currentExam.getExamDate(),
                currentExam.getExamTime(),
                assignmentId
        );

        // 3. If they are busy, throw an HTTP 409 Conflict Exception!
        if (isDoubleBooked) {
            String errorMessage = "Double Booking Warning: " + invigilatorName +
                    " is already assigned to another room on " + currentExam.getExamDate() +
                    " during the " + currentExam.getExamTime() + " slot.";

            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
        }

        targetAssignment.setRoom(newRoom);
        targetAssignment.setInvigilator(newInvigilator);
        assignmentRepo.save(targetAssignment);
    }

    public List<InvigilatorDutyResponse> getInvigilatorDuties(String invigilatorName) {
        List<InvigilatorAssignment> assignments = assignmentRepo.findByInvigilator_InvigilatorName(invigilatorName);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No assignments found for Invigilator: " + invigilatorName);
        }

        List<InvigilatorDutyResponse> duties = new ArrayList<>();

        for (InvigilatorAssignment assignment : assignments) {
            duties.add(new InvigilatorDutyResponse(
                    assignment.getExam().getSubject(),
                    assignment.getExam().getExamDate(),
                    assignment.getExam().getExamTime().name(),
                    assignment.getRoom().getRoomName(),
                    assignment.getRoom().getRoomId()
            ));
        }

        return duties;
    }


}