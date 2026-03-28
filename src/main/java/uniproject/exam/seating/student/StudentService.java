package uniproject.exam.seating.student;

import org.springframework.stereotype.Service;
import uniproject.exam.seating.seating.SeatingRepository;

import java.util.List;


@Service
public class StudentService {
    private StudentRepository studentRepository;
    private SeatingRepository seatingRepository;

    public StudentService(StudentRepository studentRepository, SeatingRepository seatingRepository) {
        this.studentRepository = studentRepository;
        this.seatingRepository = seatingRepository;
    }

    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    public void addStudent(String roll_no, String name, String major_id) {
        Student student = new Student(roll_no, name, major_id);
        studentRepository.save(student);
    }

    public void deleteStudentByRollNo(String RollNo) {
        seatingRepository.deleteByStudent_RollNo(RollNo);
        studentRepository.deleteById(RollNo);
    }

    public void  updateStudentByRollNo(String rollNo, String name, String major_id) {
        Student student = new  Student(rollNo, name, major_id);
        studentRepository.save(student);
    }

    public void resetAllStudent() {
        studentRepository.resetAllStudent();
    }

    public void resetRoomSelection(Integer roomId) {
        studentRepository.resetStudentByRoom(roomId);
    }
}
