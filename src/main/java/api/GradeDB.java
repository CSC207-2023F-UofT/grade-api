package api;

import entity.Grade;
import entity.Team;
import org.json.JSONException;

public interface GradeDB {
    // Question: The dependency can go inwards, so I feel we can use the Grade class here (which is entity).
    Grade getGrade(String utorid, String course);

    Grade logGrade(String course, int grade) throws JSONException;

    Team formTeam(String name) throws JSONException;

    Team joinTeam(String name) throws JSONException;

    Team getMyTeam() throws JSONException;

    void leaveTeam() throws JSONException;

}
