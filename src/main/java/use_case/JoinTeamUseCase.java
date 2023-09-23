package use_case;
import entity.Team;
import api.GradeDB;

public final class JoinTeamUseCase {
    private final GradeDB gradeDB;

    public JoinTeamUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public Team joinTeam(String name) {
        return gradeDB.joinTeam(name);
    }
}
