package use_case;
import api.GradeDB;
import entity.Grade;
import entity.Team;

public final class GetAverageGradeUseCase {
    private final GradeDB gradeDB;

    public GetAverageGradeUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public float getAverageGrade(String course) {
        // TODO: Get average grade for all students in your team.
        String[] members = gradeDB.getMyTeam().getMembers();
        float sumGrades = 0;
        int memberNumber = members.length;
        for (String utorid: members){
            int grade = gradeDB.getGrade(utorid, course).getGrade();
            sumGrades += grade;
        }
        return (sumGrades/memberNumber);
    }
}
