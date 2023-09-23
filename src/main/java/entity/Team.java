package entity;

public class Team {

    // Refer to the API documentation for the meaning of these fields.
    private String name;
    private String[] members;

    public Team(String name, String[] members) {
        this.name = name;
        this.members = members;
    }

    public static TeamBuilder builder() {
        return new TeamBuilder();
    }

    public static class TeamBuilder {
        private String name;
        private String[] members;

        TeamBuilder() {
        }

        public TeamBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TeamBuilder members(String[] members) {
            this.members = members;
            return this;
        }

        public Team build() {
            return new Team(name, members);
        }
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", memberLength='" + members.length + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String[] getMembers() {
        return members;
    }
}
