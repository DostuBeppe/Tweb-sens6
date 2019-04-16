package it.unito.sen6.model;

public class Scheduler {
    private Course course;
    private boolean[] schedule;
    private int availableLessons;

    public Scheduler(Course course) {
        this.course = course;
        this.schedule = new boolean[20];
        for(int i=0;i<20;i++) {
            schedule[i] = true;
        }
        this.availableLessons = 20;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean[] getSchedule() {
        return schedule;
    }

    public void setSchedule(boolean[] schedule) {
        this.schedule = schedule;
    }

    public void setFalse(int i){
        if(i<20&&schedule[i]){
            schedule[i]=false;
            availableLessons--;
        }
    }
    public int getAvailableLessons() {
        return availableLessons;
    }
    public void setAvailableLessons(int availableLessons) {
        this.availableLessons = availableLessons;
    }
}
