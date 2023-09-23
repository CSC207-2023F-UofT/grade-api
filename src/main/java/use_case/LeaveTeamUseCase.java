package use_case;
import api.GradeDB;

public final class LeaveTeamUseCase {
    private final GradeDB gradeDB;

    public LeaveTeamUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public void leaveTeam() {
        gradeDB.leaveTeam();
    }
}
