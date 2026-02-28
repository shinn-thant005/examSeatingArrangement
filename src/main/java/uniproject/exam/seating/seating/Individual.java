package uniproject.exam.seating.seating;

import uniproject.exam.seating.student.Student;

public class Individual {
    // This is the actual grid of the room
    public Student[][] seating;

    // This stores the 'score' of this specific arrangement
    public int fitness;

    public Individual(int rows, int cols) {
        // Initialize the grid size based on the Room's capacity
        this.seating = new Student[rows][cols];
        this.fitness = 0;
    }
}
