package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Event{
   private String name;
   
   public void setName(String value) {
    this.name = value;
    }
@Id
public String getName() {
    return this.name;
       }
   /**
    * <pre>
    *           1..1     1..1
    * Event ------------------------> Date
    *           &lt;       date
    * </pre>
    */
   private Date date;
   
   public void setDate(Date value) {
      this.date = value;
   }
   
   public Date getDate() {
      return this.date;
   }
   
   /**
    * <pre>
    *           1..1     1..1
    * Event ------------------------> Time
    *           &lt;       startTime
    * </pre>
    */
   private Time startTime;
   
   public void setStartTime(Time value) {
      this.startTime = value;
   }
   
   public Time getStartTime() {
      return this.startTime;
   }
   
   /**
    * <pre>
    *           1..1     1..1
    * Event ------------------------> Time
    *           &lt;       endTime
    * </pre>
    */
   private Time endTime;
   
   public void setEndTime(Time value) {
      this.endTime = value;
   }
   
   public Time getEndTime() {
      return this.endTime;
   }
   
   }
