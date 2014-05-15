import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a sports division and determines which teams are
 * mathematically eliminated.
 *
 * @author Brian Terczynski
 */
public class BaseballElimination {
    /**
     * The number of teams.
     */
    private int numTeams;
    /**
     * Map of team names to team indices.
     */
    private Map<String, Integer> teams;
    /**
     * The team names.
     */
    private String[] teamNames;
    /**
     * The team wins.
     */
    private int [] wins;
    /**
     * The team losses.
     */
    private int [] losses;
    /**
     * The number of games remaining.
     */
    private int [] gamesRemaining;
    /**
     * The game matchups.
     */
    private int [][] gamesMatrix;

    /**
     * Creates a baseball division from the given filename.
     *
     * @param filename
     *     The file from which to create the baseball division.
     */
    public BaseballElimination(String filename) {
        In file = new In(filename);
        numTeams = file.readInt();
        teams = new HashMap<String, Integer>();
        wins = new int [numTeams];
        losses = new int [numTeams];
        gamesRemaining = new int [numTeams];
        teamNames = new String [numTeams];
        gamesMatrix = new int [numTeams][numTeams];
        for (int i = 0; i < numTeams; i++) {
            String teamName = file.readString();
            teams.put(teamName, i);
            teamNames[i] = teamName;
            wins[i] = file.readInt();
            losses[i] = file.readInt();
            gamesRemaining[i] = file.readInt();
            for (int j = 0; j < numTeams; j++) {
                gamesMatrix[i][j] = file.readInt();
            }
        }
    }

    /**
     * The number of teams.
     *
     * @return
     *     The the number of teams.
     */
    public int numberOfTeams() {
        return numTeams;
    }

    /**
     * Returns the teams in the division.
     *
     * @return
     *     The names of the teams in the division.
     */
    public Iterable<String> teams() {
        return teams.keySet();
    }

    /**
     * The number of wins for the given team.
     *
     * @param team
     *     The team.
     *
     * @return
     *     The number of wins for the team.
     */
    public int wins(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return wins [teams.get(team)];
    }

    /**
     * The number of losses for the given team.
     *
     * @param team
     *     The number of losses for the given team.
     *
     * @return
     *     The number of losses.
     */
    public int losses(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return losses [teams.get(team)];
    }

    /**
     * Returns the number of remaining games for the given team.
     *
     * @param team
     *     The team.
     *
     * @return
     *     The number of remaining games.
     */
    public int remaining(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return gamesRemaining [teams.get(team)];
    }

    /**
     * Returns the number of games remaining between team1 and team2.
     *
     * @param team1
     *     Team 1.
     * @param team2
     *     Team 2.
     *
     * @return
     *     The number of games remaining between the two teams.
     */
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return gamesMatrix[teams.get(team1)][teams.get(team2)];
    }

    /**
     * Returns whether the given team is mathematically eliminated.
     *
     * @param team
     *     The team to check.
     *
     * @return
     *     True if the team is mathematically eliminated; false if not.
     */
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamInQuestion = teams.get(team);
        List<String> trivialElim = computeTrivialElimination(teamInQuestion);
        if (trivialElim.size() > 0) {
            return true;
        }
        TeamFlowNetwork teamFlowNetwork =
                generateTeamFlowNetwork(teamInQuestion);
//        System.out.println (teamFlowNetwork);

        return teamFlowNetwork.isEliminated();
    }

    /**
     * Returns the teams that eliminate the given team, or null if the team
     * is not eliminated.
     *
     * @param team
     *     The team to check.
     * @return
     *     The teams that eliminate the given team, or null if the team is not
     *     mathematically eliminated.
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamInQuestion = teams.get(team);
        List<String> trivialElim = computeTrivialElimination(teamInQuestion);
        if (trivialElim.size() > 0) {
            return trivialElim;
        }
        TeamFlowNetwork teamFlowNetwork =
                generateTeamFlowNetwork(teamInQuestion);

        if (teamFlowNetwork.isEliminated()) {
            List<String> eliminatingTeams = new ArrayList<String>();
            for (int teamIndex : teamFlowNetwork.eliminatingTeams()) {
                eliminatingTeams.add(teamNames[teamIndex]);
            }
            return eliminatingTeams;
        } else {
            return null;
        }
    }

    /**
     * Returns which teams trivially eliminate the given team, or the empty
     * list if the team is not trivially eliminated.
     *
     * @param teamInQuestion
     *     The team in question.
     * @return
     *     The list of trivially eliminating teams; or empty if there are none.
     */
    private List<String> computeTrivialElimination(int teamInQuestion) {
        List<String> eliminatingTeams = new ArrayList<String>();
        for (int i = 0; i < numTeams; i++) {
            if (i != teamInQuestion) {
                if ((wins[teamInQuestion]
                + gamesRemaining[teamInQuestion]
                - wins[i]) < 0) {
                    eliminatingTeams.add(teamNames[i]);
                }
            }
        }
        return eliminatingTeams;
    }

    /**
     * Generates a team flow network.
     *
     * @param teamInQuestion
     *     The team in question.
     * @return
     *     The team flow network.
     */
    private TeamFlowNetwork generateTeamFlowNetwork(int teamInQuestion) {
        ////////////////////////////////////////////
        //  Num vertices: 1 + (N-1)(N-2)/2 + (N-1) + 1 =
        //                1 + N + (N-1)(N-2)/2
        ////////////////////////////////////////////
        FlowNetwork flowNetwork = new FlowNetwork(((numTeams - 1)
                * (numTeams - 2) / 2) + numTeams + 1);
        //////////////////////////////////////
        //  Maps a game vertex to a team pair.
        //////////////////////////////////////
        Map<Integer, Pair> gameVertices = new HashMap<Integer, Pair>();
        /////////////////////////////////
        //  Maps a team to a team vertex.
        /////////////////////////////////
        Map<Integer, Integer> teamVertices = new HashMap<Integer, Integer>();
        ///////////////////////////////////////////////////////
        //  Create the game vertices, and attach to the source.
        ///////////////////////////////////////////////////////
        int curVertex = 1;
        int capacity = 0;
        for (int i = 0; i < numTeams; i++) {
            for (int j = i + 1; j < numTeams; j++) {
                if (i != teamInQuestion && j != teamInQuestion) {
                    flowNetwork.addEdge(new FlowEdge(0, curVertex,
                            gamesMatrix[i][j]));
                    capacity += gamesMatrix[i][j];
                    gameVertices.put(curVertex, new Pair(i, j));
                    curVertex++;
                }
            }
        }
        /////////////////////////////////////////////////////
        //  Create the team vertices, and attach to the sink.
        /////////////////////////////////////////////////////
        for (int i = 0; i < numTeams; i++) {
            if (i != teamInQuestion) {
                teamVertices.put(i, curVertex);
                flowNetwork.addEdge(new FlowEdge(curVertex, flowNetwork.V() - 1,
                        wins[teamInQuestion]
                        + gamesRemaining[teamInQuestion]
                        - wins[i]));
                curVertex++;
            }
        }
        //////////////////////////////////////////////////
        //  Attach the game vertices to the team vertices.
        //////////////////////////////////////////////////
        for (Map.Entry<Integer, Pair> entry : gameVertices.entrySet()) {
            int gameVertex = entry.getKey();
            int teamVertex1 = teamVertices.get(entry.getValue().getOne());
            int teamVertex2 = teamVertices.get(entry.getValue().getTwo());
            flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex1,
                    Double.POSITIVE_INFINITY));
            flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex2,
                    Double.POSITIVE_INFINITY));
        }
        return new TeamFlowNetwork(flowNetwork, gameVertices, teamVertices,
                capacity);
    }

    /**
     * Represents a pair of integers.
     *
     * @author Brian Terczynski
     */
    private class Pair {
        /**
         * Number one.
         */
        private int one;
        /**
         * Number two.
         */
        private int two;

        /**
         * Constructs the pair.
         *
         * @param one
         *     Number one.
         * @param two
         *     Number two.
         */
        public Pair(int one, int two) {
            this.one = one;
            this.two = two;
        }

        /**
         * Returns number one.
         *
         * @return
         *     Number one.
         */
        public int getOne() {
            return one;
        }

        /**
         * Returns number two.
         *
         * @return
         *     Number two.
         */
        public int getTwo() {
            return two;
        }

        /**
         * String representation.
         *
         * @return
         *     The string representation.
         */
        @Override
        public String toString() {
            return "(" + one + "," + two + ")";
        }
    }

    /**
     * Represents a team flow network.
     *
     * @author Brian Terczynski
     */
    private class TeamFlowNetwork {
        /**
         * The flow network.
         */
        private FlowNetwork flowNetwork;
        /**
         * Map of game vertices to the pair of teams in the game.
         */
        private Map<Integer, Pair> gameVertices;
        /**
         * Map of teams to team vertices.
         */
        private Map<Integer, Integer> teamVertices;
        /**
         * The capacity of all edges from the source.
         */
        private int capacity;
        /**
         * Ford-Fulkerson data structure.
         */
        private FordFulkerson ff;

        /**
         * Constructs the Team Flow Network.
         *
         * @param flowNetwork
         *     The flow network.
         * @param gameVertices
         *     The game vertices.
         * @param teamVertices
         *     The team vertices.
         * @param capacity
         *     The capacity.
         */
        public TeamFlowNetwork (FlowNetwork flowNetwork,
                Map<Integer, Pair> gameVertices,
                Map<Integer, Integer> teamVertices,
                int capacity) {
            this.flowNetwork = flowNetwork;
            this.gameVertices = gameVertices;
            this.teamVertices = teamVertices;
            this.capacity = capacity;
            ff = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        }

        /**
         * Returns true if the team is eliminated.
         *
         * @return
         *     True if the team is eliminated; false if not.
         */
        public boolean isEliminated() {
            return ff.value() < capacity;
        }

        /**
         * Returns the list of eliminating teams.
         *
         * @return
         *     The eliminating teams, or an empty iteration if there are none.
         */
        public Iterable<Integer> eliminatingTeams() {
            List<Integer> eliminatingTeams = new ArrayList<Integer>();
            for (Map.Entry<Integer, Integer> entry : teamVertices.entrySet()) {
                int team = entry.getKey();
                int vertex = entry.getValue();
                if (ff.inCut(vertex)) {
                    eliminatingTeams.add(team);
                }
            }
            return eliminatingTeams;
        }

        /**
         * Returns a string representation.
         *
         * @return
         *     The string representation.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(flowNetwork.toString()).append("\n");
            for (Map.Entry<Integer, Pair> entry : gameVertices.entrySet()) {
                sb.append(entry.getKey()).append(":").
                    append(entry.getValue()).append("\n");
            }
            for (Map.Entry<Integer, Integer> entry : teamVertices.entrySet()) {
                sb.append(entry.getKey()).append(":").
                    append(entry.getValue()).append("\n");
            }
            sb.append("Flow=").append(ff.value()).append("\n");
            return sb.toString();
        }
    }

    /**
     * Main test method.
     *
     * @param args
     *     Specifies the input file.
     */
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

        /*for (String team : division.teams()) {
            System.out.print(team + " " + division.wins(team) + " " +
                    division.losses(team) + " " + division.remaining(team) +
                    " ");
            for (String team2 : division.teams()) {
                System.out.print(division.against(team, team2) + " ");
            }
            System.out.println();
        }*/
    }
}
