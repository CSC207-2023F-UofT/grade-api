package use_case;

import api.GradeDB;
import entity.Team;

public class MaxGradeUseCase {
    private final GradeDB gradeDB;

    public MaxGradeUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public float getHighestGrade(String course) {
        Team currTeam = this.gradeDB.getMyTeam();
        String[] teamMembers = currTeam.getMembers();
        int highest = this.gradeDB.getGrade(teamMembers[0], course).getGrade();
        for (String teamMember : teamMembers) {
            int currGrade = this.gradeDB.getGrade(teamMember, course).getGrade();
            if (currGrade > highest) {
                highest = currGrade;
            }
        }
        return highest;
    }
}
