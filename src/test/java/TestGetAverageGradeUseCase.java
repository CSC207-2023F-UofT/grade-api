import api.GradeDB;
import entity.Grade;
import entity.Team;
import org.json.JSONException;
import org.junit.Test;
import use_case.GetAverageGradeUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TestGetAverageGradeUseCase {
    private static class TestGradeDB implements GradeDB {
        private HashMap<String, Integer> grades = new HashMap<>();
        private String[] members;

        public TestGradeDB(String[] members) {
            this.members = members;
        }

        public void addGrade(String user, int grade) {
            grades.put(user, grade);
        }

        @Override
        public Grade getGrade(String utorid, String course) {
            Integer value = grades.get(utorid);

            if (value == null) {
                return null;
            }

            return new Grade(utorid, course, value);
        }

        @Override
        public Grade logGrade(String course, int grade) throws JSONException {
            throw new RuntimeException("not implemented");
        }

        @Override
        public Team formTeam(String name) throws JSONException {
            throw new RuntimeException("not implemented");
        }

        @Override
        public Team joinTeam(String name) throws JSONException {
            throw new RuntimeException("not implemented");
        }

        @Override
        public Team getMyTeam() throws JSONException {
            return new Team("foobar", members);
        }

        @Override
        public void leaveTeam() throws JSONException {
            throw new RuntimeException("not implemented");
        }
    }

    @Test
    public void noMembersHasZeroGrade() {
        GradeDB db = new TestGradeDB(new String[] {});
        assertEquals(0.0f, new GetAverageGradeUseCase(db).getAverageGrade("207"), 1e-6);
    }

    @Test
    public void membersMissingGradeHasZeroAverage() {
        GradeDB db = new TestGradeDB(new String[] { "abc", "def" });
        assertEquals(0.0f, new GetAverageGradeUseCase(db).getAverageGrade("207"), 1e-6);
    }

    @Test
    public void oneAverageTakesThatValue() {
        TestGradeDB db = new TestGradeDB(new String[] { "abc", "def" });
        db.addGrade("abc", 50);
        assertEquals(50.0f, new GetAverageGradeUseCase(db).getAverageGrade("207"), 1e-6);
    }

    @Test
    public void twoMembersHaveCorrectAverage() {
        TestGradeDB db = new TestGradeDB(new String[] { "abc", "def" });
        db.addGrade("abc", 50);
        db.addGrade("def", 100);
        assertEquals(75.0f, new GetAverageGradeUseCase(db).getAverageGrade("207"), 1e-6);
    }
}
