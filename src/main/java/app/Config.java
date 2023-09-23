package app;

import api.GradeDB;
import api.MongoGradeDB;
import use_case.*;

public class Config {
    private final GradeDB gradeDB = new MongoGradeDB();

    public GetGradeUseCase getGradeUseCase() {
        return new GetGradeUseCase(gradeDB);
    }

    public LogGradeUseCase logGradeUseCase() {
        return new LogGradeUseCase(gradeDB);
    }

    public FormTeamUseCase formTeamUseCase () {
        return new FormTeamUseCase(gradeDB);
    }

    public JoinTeamUseCase joinTeamUseCase () {
        return new JoinTeamUseCase(gradeDB);
    }

    public LeaveTeamUseCase leaveTeamUseCase () {
        return new LeaveTeamUseCase(gradeDB);
    }

    public GetAverageGradeUseCase getAverageGradeUseCase () {
        return new GetAverageGradeUseCase(gradeDB);
    }
}
