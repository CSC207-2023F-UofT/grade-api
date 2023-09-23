package entity;

public class Grade {

    // Refer to the API documentation for the meaning of these fields.
    private String utorid;
    private String course;
    private int grade;

    public Grade(String utorid, String course, int grade) {
        this.utorid = utorid;
        this.course = course;
        this.grade = grade;
    }

    public static GradeBuilder builder() {
        return new GradeBuilder();
    }

    public static class GradeBuilder {
        private String utorid;
        private String course;
        private int grade;

        GradeBuilder() {
        }

        public GradeBuilder utorid(String utorid) {
            this.utorid = utorid;
            return this;
        }

        public GradeBuilder course(String course) {
            this.course = course;
            return this;
        }

        public GradeBuilder grade(int grade) {
            this.grade = grade;
            return this;
        }

        public Grade build() {
            return new Grade(utorid, course, grade);
        }
    }

    @Override
    public String toString() {
        return "Grade{" +
                "utorid='" + utorid + '\'' +
                ", course='" + course + '\'' +
                ", grade=" + grade +
                '}';
    }

    public String getUtorid() {
        return utorid;
    }

    public String getCourse() {
        return course;
    }

    public int getGrade() {
        return grade;
    }




}
