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
        Team myteam = gradeDB.getMyTeam();
        float total = 0.0f;
        for (String member: myteam.getMembers()){
            total += gradeDB.getGrade(member, course).getGrade();
        }
        return total / myteam.getMembers().length;
    }
}
