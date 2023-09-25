package use_case;
import api.GradeDB;
import entity.Team;

public final class GetAverageGradeUseCase {
    private final GradeDB gradeDB;

    public GetAverageGradeUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public float getAverageGrade(String course) {
        Team currTeam = this.gradeDB.getMyTeam();
        String[] teamMembers = currTeam.getMembers();
        float summy = 0;
        for (String teamMember : teamMembers) {
            int currGrade = this.gradeDB.getGrade(teamMember, course).getGrade();
            summy += currGrade;
        }
        return summy / teamMembers.length;
    }
}
