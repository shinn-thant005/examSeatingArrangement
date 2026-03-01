package uniproject.exam.seating.seating;

import org.springframework.stereotype.Service;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.room.roomRepository;
import uniproject.exam.seating.student.Student;
import uniproject.exam.seating.student.studentRepository;

import java.util.*;

@Service
public class seatingService {

    private final seatingRepository seatingRepo;
    private final studentRepository studentRepo;
    private final roomRepository roomRepo;

    // GA Parameters
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 300;
    private static final double MUTATION_RATE = 0.1;
    private static final int TOURNAMENT_SIZE = 5;

    public seatingService(studentRepository studentRepo, roomRepository roomRepo,  seatingRepository seatingRepository) {
        this.studentRepo = studentRepo;
        this.roomRepo = roomRepo;
        this.seatingRepo = seatingRepository;
    }

    // --- 1. THE MAIN CONTROLLER METHOD ---
    public SeatingPlanResponse generateSeatingPlan(Integer roomId) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        int rows = room.getRowCapacity();
        int cols = room.getColumnCapacity();
        int roomCapacity = rows * cols;

        // --- NEW LOGIC: Only fetch students who are NOT seated yet ---
        List<Student> availableStudents = studentRepo.findByIsSeatedFalse();

        if (availableStudents.isEmpty()) {
            throw new RuntimeException("No unseated students available in the database!");
        }

        // --- NEW LOGIC: If there are more students than seats, take only what fits ---
        List<Student> studentsToSeat;
        if (availableStudents.size() > roomCapacity) {
            studentsToSeat = availableStudents.subList(0, roomCapacity);
        } else {
            studentsToSeat = availableStudents;
        }

        // Initialize Population
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Individual(rows, cols, studentsToSeat));
        }

        // Evolution Loop
        for (int gen = 0; gen < GENERATIONS; gen++) {
            // Calculate Fitness
            for (Individual ind : population) {
                ind.fitness = calculateFitness(ind, rows, cols);
            }

            // Sort by fitness (Descending - higher is better)
            population.sort((a, b) -> Integer.compare(b.fitness, a.fitness));

            List<Individual> newPopulation = new ArrayList<>();

            // Elitism: Keep the best 10% automatically
            int eliteCount = POPULATION_SIZE / 10;
            for (int i = 0; i < eliteCount; i++) {
                newPopulation.add(new Individual(population.get(i)));
            }

            // Fill the rest with Crossover and Mutation
            while (newPopulation.size() < POPULATION_SIZE) {
                Individual parent1 = tournamentSelection(population);
                Individual parent2 = tournamentSelection(population);

                Individual child = crossover(parent1, parent2, rows, cols);

                if (Math.random() < MUTATION_RATE) {
                    mutate(child, rows, cols);
                }

                newPopulation.add(child);
            }
            population = newPopulation;
        }

        // Final evaluation to get the absolute best
        for (Individual ind : population) {
            ind.fitness = calculateFitness(ind, rows, cols);
        }
        population.sort((a, b) -> Integer.compare(b.fitness, a.fitness));
        Individual bestSolution = population.get(0);

        // --- NEW LOGIC: Mark the successfully seated students as true in the database ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Student s = bestSolution.seating[r][c];
                if (s != null) {
                    s.setSeated(true);
                    s.setAssignedRoom(room);
                    studentRepo.save(s); // Update database

                    // 2. Save the exact seat coordinates to the Seating table (NEW LOGIC)
                    Seating seatingRecord = new Seating();
                    seatingRecord.setStudent(s);
                    seatingRecord.setRoom(room);
                    seatingRecord.setRowNum(r);
                    seatingRecord.setColumnNum(c);
                    seatingRepo.save(seatingRecord);
                }
            }
        }

        return convertToResponse(room, bestSolution, rows, cols);
    }

    // --- ADD THIS BELOW YOUR generateSeatingPlan METHOD ---

    public SeatingPlanResponse getSavedSeatingPlan(Integer roomId) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        // Fetch the saved coordinates from the database
        List<Seating> savedSeats = seatingRepo.findByRoom_RoomId(roomId);

        if (savedSeats.isEmpty()) {
            throw new RuntimeException("No seating plan found for this room. Please generate it first!");
        }

        int rows = room.getRowCapacity();
        int cols = room.getColumnCapacity();

        // Create an empty grid
        String[][] gridArray = new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                gridArray[r][c] = "EMPTY";
            }
        }

        // Fill the grid using the coordinates saved in the database
        for (Seating seat : savedSeats) {
            Student s = seat.getStudent();
            gridArray[seat.getRowNum()][seat.getColumnNum()] = s.getRollNo() + " (" + s.getMajorId() + ")";
        }

        // Convert to the Response DTO for the frontend
        SeatingPlanResponse response = new SeatingPlanResponse();
        response.setRoomName(room.getRoomName());
        response.setFloor(room.getFloor());

        List<List<String>> gridList = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            gridList.add(Arrays.asList(gridArray[r]));
        }
        response.setLayout(gridList);

        return response;
    }

    // --- 2. GENETIC ALGORITHM METHODS ---

    private int calculateFitness(Individual ind, int rows, int cols) {
        int fitness = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Student current = ind.seating[r][c];
                if (current == null) continue;

                // Check right neighbor
                if (c + 1 < cols && ind.seating[r][c + 1] != null) {
                    if (!current.getMajorId().equals(ind.seating[r][c + 1].getMajorId())) {
                        fitness++; // Different majors sitting next to each other is GOOD
                    } else {
                        fitness--; // Same major sitting next to each other is BAD
                    }
                }
                // Check bottom neighbor
                if (r + 1 < rows && ind.seating[r + 1][c] != null) {
                    if (!current.getMajorId().equals(ind.seating[r + 1][c].getMajorId())) {
                        fitness++;
                    } else {
                        fitness--;
                    }
                }
            }
        }
        return fitness;
    }

    private Individual tournamentSelection(List<Individual> population) {
        Individual best = null;
        Random rand = new Random();
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual competitor = population.get(rand.nextInt(population.size()));
            if (best == null || competitor.fitness > best.fitness) {
                best = competitor;
            }
        }
        return best; // Return a reference (we copy it in crossover)
    }

    private Individual crossover(Individual parent1, Individual parent2, int rows, int cols) {
        Individual child = new Individual(rows, cols);
        Set<String> placedStudentIds = new HashSet<>();
        List<Student> missingStudents = new ArrayList<>();

        // Take top half of the room from Parent 1
        for (int r = 0; r < rows / 2; r++) {
            for (int c = 0; c < cols; c++) {
                Student s = parent1.seating[r][c];
                child.seating[r][c] = s;
                if (s != null) placedStudentIds.add(s.getRollNo());
            }
        }

        // Scan Parent 2 for students not yet placed
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Student s = parent2.seating[r][c];
                if (s != null && !placedStudentIds.contains(s.getRollNo())) {
                    missingStudents.add(s);
                }
            }
        }

        // Fill the bottom half of the room with the missing students
        int missingIdx = 0;
        for (int r = rows / 2; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (missingIdx < missingStudents.size()) {
                    child.seating[r][c] = missingStudents.get(missingIdx++);
                } else {
                    child.seating[r][c] = null; // Empty seat
                }
            }
        }
        return child;
    }

    private void mutate(Individual ind, int rows, int cols) {
        Random rand = new Random();
        int r1 = rand.nextInt(rows);
        int c1 = rand.nextInt(cols);
        int r2 = rand.nextInt(rows);
        int c2 = rand.nextInt(cols);

        // Swap two random seats (could be a student and an empty seat, or two students)
        Student temp = ind.seating[r1][c1];
        ind.seating[r1][c1] = ind.seating[r2][c2];
        ind.seating[r2][c2] = temp;
    }

    // --- 3. DTO CONVERSION ---

    private SeatingPlanResponse convertToResponse(Room room, Individual best, int rows, int cols) {
        SeatingPlanResponse response = new SeatingPlanResponse();
        response.setRoomName(room.getRoomName());
        response.setFloor(room.getFloor());

        List<List<String>> grid = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            List<String> rowList = new ArrayList<>();
            for (int c = 0; c < cols; c++) {
                Student s = best.seating[r][c];
                rowList.add(s != null ? s.getRollNo() + " (" + s.getMajorId() + ")" : "EMPTY");
            }
            grid.add(rowList);
        }
        response.setLayout(grid);
        return response;
    }

    // --- 4. INNER CLASSES ---

    public static class Individual {
        public Student[][] seating;
        public int fitness;

        // Create empty individual
        public Individual(int rows, int cols) {
            this.seating = new Student[rows][cols];
            this.fitness = 0;
        }

        // Create individual with random initial population
        public Individual(int rows, int cols, List<Student> students) {
            this.seating = new Student[rows][cols];
            this.fitness = 0;

            List<Student> shuffled = new ArrayList<>(students);
            Collections.shuffle(shuffled);

            int idx = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (idx < shuffled.size()) {
                        this.seating[r][c] = shuffled.get(idx++);
                    }
                }
            }
        }

        // Copy constructor for Elitism
        public Individual(Individual other) {
            int rows = other.seating.length;
            int cols = other.seating[0].length;
            this.seating = new Student[rows][cols];
            for (int r = 0; r < rows; r++) {
                System.arraycopy(other.seating[r], 0, this.seating[r], 0, cols);
            }
            this.fitness = other.fitness;
        }
    }

    public static class SeatingPlanResponse {
        private String roomName;
        private Integer floor;
        private List<List<String>> layout;

        public String getRoomName() { return roomName; }
        public void setRoomName(String roomName) { this.roomName = roomName; }
        public Integer getFloor() { return floor; }
        public void setFloor(Integer floor) { this.floor = floor; }
        public List<List<String>> getLayout() { return layout; }
        public void setLayout(List<List<String>> layout) { this.layout = layout; }
    }


    public void deleteSeatingPlan(Integer SeatingId) {
        Seating currentSeating = seatingRepo.findById(SeatingId).orElse(null);
        Student student = currentSeating.getStudent();
        student.setSeated(false);
        student.setAssignedRoom(null);
        seatingRepo.deleteById(SeatingId);
    }

    public void deleteSeatingPlanByRoomId(Integer roomId) {
        studentRepo.resetStudentByRoom(roomId);
        seatingRepo.deleteAllByRoom_RoomId(roomId);
    }

    public void updateSeatingPlan(Integer seatingId, String rollNo, String roomName, Integer rowNum, Integer columnNum) {
        Seating existingSeating = seatingRepo.findById(seatingId)
                .orElseThrow(() -> new RuntimeException("Seating record not found with ID: " + seatingId));

        Student student = studentRepo.findById(rollNo)
                .orElseThrow(() -> new RuntimeException("Student not found with Roll No: " + rollNo));

        Room room = roomRepo.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("Room not found with Room Name: " + roomName));

        existingSeating.setStudent(student);
        existingSeating.setRoom(room);
        existingSeating.setRowNum(rowNum);
        existingSeating.setColumnNum(columnNum);
        seatingRepo.save(existingSeating);

        student.setAssignedRoom(room);
        student.setSeated(true);
        studentRepo.save(student);

    }
}