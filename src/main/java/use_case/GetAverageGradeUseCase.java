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
        Team t = gradeDB.getMyTeam();
        String[] members = t.getMembers();
        float gradeSum = 0;

        for (String member : members) {
            gradeSum += gradeDB.getGrade(member, course).getGrade();
        }
        return gradeSum / members.length;
    }
}