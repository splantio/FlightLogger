package ca.lakeland.plantsd.flightlogger;

/**
 * Created by plantsd on 5/20/2016.
 */
public class Pilot {

    String name;
    int takeoffsAndLandings;
    int flightTime;

    public Pilot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTakeoffsAndLandings() {
        return takeoffsAndLandings;
    }

    public void setTakeoffsAndLandings(int takeoffsAndLandings) {
        this.takeoffsAndLandings = takeoffsAndLandings;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }
}
