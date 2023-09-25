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

        String[] members = myteam.getMembers();
        float total=0;


        for(int i =0;i < members.length; i++){
            System.out.println(members[i]);
            System.out.println(total);
            total = total + gradeDB.getGrade(members[i], course).getGrade();

        }

        return total/ members.length;
    }
}
