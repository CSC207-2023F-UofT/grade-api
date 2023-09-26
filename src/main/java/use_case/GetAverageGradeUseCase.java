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

        Team team = this.gradeDB.getMyTeam();
        String[] members = team.getMembers();

        float sum = 0;
        for (String member: members) {
            Grade grade = this.gradeDB.getGrade(member, course);
            sum += grade.getGrade();
        }

        return sum / members.length;
    }
}
