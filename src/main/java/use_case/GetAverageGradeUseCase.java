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
        Team team = gradeDB.getMyTeam();
        float total = 0f;
        for (String member : team.getMembers()){
            total += gradeDB.getGrade(member, course).getGrade();
        }
        return total / team.getMembers().length;
    }
}
