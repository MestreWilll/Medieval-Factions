package dansplugins.factionsystem.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dansplugins.factionsystem.MedievalFactions;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerActivityRecord {
    private UUID playerUUID = null;
    private int logins = 0;
    private int powerLost = 0;
    private ZonedDateTime lastLogout = ZonedDateTime.now();
    
    public PlayerActivityRecord(UUID uuid, int logins)
    {
    	playerUUID = uuid;
    	this.logins = logins;
    	this.powerLost = 0;
    }

    public void setPowerLost(int power)
    {
    	powerLost = power;
    }
    
    public int getPowerLost()
    {
    	return powerLost;
    }
    
    public void incrementPowerLost()
    {
    	powerLost += MedievalFactions.getInstance().getConfig().getInt("powerDecreaseAmount");
    }
    
    public void setPlayerUUID(UUID uuid) {
        playerUUID = uuid;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setLastLogout(ZonedDateTime date) {
        lastLogout = date;
    }

    public ZonedDateTime getLastLogout() {
        return lastLogout;
    }

    public void incrementLogins() {
        logins++;
//        System.out.println("Incrementing logins for uuid " + getPlayerUUID().toString() + ": logins=" + Integer.toString(logins));
    }

    public int getLogins() {
        return logins;
    }
    
    public PlayerActivityRecord(Map<String, String> data) {
        this.load(data);
    }
    
    public int getMinutesSinceLastLogout()
    {
    	if (lastLogout != null)
    	{
    		ZonedDateTime now = ZonedDateTime.now();
    		Duration duration = Duration.between(lastLogout, now);
    		double totalSeconds = duration.getSeconds();
    		int minutes = (int)totalSeconds / 60;
    		return minutes;
    	}
    	return 0;
    }

    public String getTimeSinceLastLogout() {
        if (lastLogout != null) {
            ZonedDateTime now = ZonedDateTime.now();
            Duration duration = Duration.between(lastLogout, now);
            double totalSeconds = duration.getSeconds();
            int minutes = (int) totalSeconds/60;
            int hours = minutes / 60;
            int days = hours / 24;
            int hoursSince = hours - (days * 24);
            int minutesSince = minutes - (hours * 60) - (days * 24 * 60); // This is no longer displayed since it displayed a negative number. -Dan 6/4/2021

            return days + " days and " + hoursSince + " hours";
        }
        else {
            return null;
        }
    }

    public Map<String, String> save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, String> saveMap = new HashMap<>();
        saveMap.put("playerUUID", gson.toJson(playerUUID.toString()));
        saveMap.put("logins", gson.toJson(logins));
        saveMap.put("lastLogout", gson.toJson(lastLogout.toString()));
        saveMap.put("powerLost", gson.toJson(powerLost));

        return saveMap;
    }   
    
    private void load(Map<String, String> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        playerUUID = UUID.fromString(gson.fromJson(data.get("playerUUID"), String.class));
        logins = gson.fromJson(data.get("logins"), Integer.TYPE);
        lastLogout = ZonedDateTime.parse(gson.fromJson(data.get("lastLogout"), String.class), DateTimeFormatter.ISO_ZONED_DATE_TIME);
        powerLost = gson.fromJson(data.get("powerLost"), Integer.TYPE);
    }

}