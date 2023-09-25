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
        float avrgGrade = 0.0F;
        String[] members = gradeDB.getMyTeam();
        for (String member : members) {
            avrgGrade += gradeDB.getGrade(member, course).getGrade();
        }
        return avrgGrade / members.length;
    }
}
