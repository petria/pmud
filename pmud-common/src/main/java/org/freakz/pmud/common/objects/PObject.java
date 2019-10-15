package org.freakz.pmud.common.objects;

import java.util.HashMap;
import java.util.Map;

public class PObject extends PMudObject {

    private Zone zone;
    private Location location;

    private String pName;
    private String altName;

    private int size;
    private int baseValue;
    private int weight;
    private int damage;
    private int armorClass;

    private int state = 0;
    private int maxState = 0;

    private String examine;

    private String oFlags;
    private Map<Integer, String> descriptions = new HashMap<>();

    private Map<Integer, PObject> contains = new HashMap<>();

    private PObject container;

    private boolean isInContainer;

    private Mobile carrier;

    private boolean isCarried;

    private Mobile wornBy;

    private boolean isWorn;

    private Mobile wieldedBy;

    private boolean isWielded;

    private Location inRoom;

    private boolean isInRoom;

    private boolean isLocked;

    private PObject linkedTo;

    //


    // PFlags
    private boolean isContainer = false;

    private boolean isExtinguish = false;

    private boolean isLit = false;

    private boolean isLockable = false;

    private boolean isNoGet = false;

    private boolean isOpenable = false;

    private boolean isWeapon = false;

    private boolean isKey = false;

    public PObject(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Location getLocation() {
        return location;
    }

    public Location location() {
        if (isInRoom) {
            return location;
        }
        if (isInContainer) {
            return container.getLocation();
        }
        return carrier.getLocation();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String where() {
        if (isInRoom) {
            return location.getTitle();
        } else {
            if (isInContainer) {
                return "Inside of " + getContainer().name();
            } else if (!isWielded && !isWorn) {
                return "carried by " + carrier.getName();
            } else {
                if (isWielded && isWorn) {
                    return "wielded and worn by " + carrier.getName();
                } else {
                    if (isWorn) {
                        return "worn by " + carrier.getName();
                    }
                    return "wielded by " + carrier.getName();
                }

            }
        }
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMaxState() {
        return maxState;
    }

    public void setMaxState(int maxState) {
        this.maxState = maxState;
    }

    public String getoFlags() {
        return oFlags;
    }

    public void setoFlags(String oFlags) {
        if (oFlags != null) {
            this.oFlags = oFlags.trim();
        } else {
            this.oFlags = "";
        }
    }

    public void flagsToProperties() {
        for (String flag : oFlags.split(" ")) {
            if (flag != null) {
                flag = flag.trim();
                if (flag.equalsIgnoreCase("Container")) {
                    isContainer = true;
                }
                if (flag.equalsIgnoreCase("Extinguish")) {
                    isExtinguish = true;
                }
                if (flag.equalsIgnoreCase("Key")) {
                    isKey = true;
                }
                if (flag.equalsIgnoreCase("Lit")) {
                    isLit = true;
                }
                if (flag.equalsIgnoreCase("Lockable")) {
                    isLockable = true;
                    if (state == 2) {
                        isLocked = true;
                    }
                }
                if (flag.equalsIgnoreCase("Openable")) {
                    isOpenable = true;
                }
                if (flag.equalsIgnoreCase("NoGet")) {
                    isNoGet = true;
                }
                if (flag.equalsIgnoreCase("Weapon")) {
                    isWeapon = true;
                }

            }

        }
    }


    public Map<Integer, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<Integer, String> descriptions) {
        this.descriptions = descriptions;
    }

    public void setDescription(int state, String description) {
        this.descriptions.put(state, description);
    }

    public String getDescription(int state) {
        String desc = this.descriptions.get(state);
        if (desc == null) {
            return "";
        }
        return desc;
    }

    public PObject getLinkedTo() {
        return linkedTo;
    }

    public void setLinkedTo(PObject linkedTo) {
        this.linkedTo = linkedTo;
    }

    public Map<Integer, PObject> getContains() {
        return contains;
    }

    public void addContains(PObject object) {
        object.setInContainer(true);
        object.setContainer(this);
        this.contains.put(object.getId(), object);
    }

    public void removeContains(PObject object) {
        object.setInContainer(false);
        object.setContainer(null);
        this.contains.remove((object.getId()));
    }

    public String name() {
        if (name != null) {
            return name.toLowerCase();
        }
        if (pName != null) {
            return pName.toLowerCase();
        }
        return altName.toLowerCase();
    }

    public PObject getContainer() {
        return container;
    }

    public void setContainer(PObject container) {
        this.container = container;
    }

    public boolean isInContainer() {
        return isInContainer;
    }

    public void setInContainer(boolean inContainer) {
        isInContainer = inContainer;
    }

    public Mobile getCarrier() {
        return carrier;
    }

    public void setCarrier(Mobile carrier) {
        this.carrier = carrier;
    }

    public boolean isCarried() {
        return isCarried;
    }

    public void setCarried(boolean carried) {
        isCarried = carried;
    }

    public Mobile getWornBy() {
        return wornBy;
    }

    public void setWornBy(Mobile wornBy) {
        this.wornBy = wornBy;
    }

    public boolean isWorn() {
        return isWorn;
    }

    public void setWorn(boolean worn) {
        isWorn = worn;
    }

    public Mobile getWieldedBy() {
        return wieldedBy;
    }

    public void setWieldedBy(Mobile wieldedBy) {
        this.wieldedBy = wieldedBy;
    }

    public boolean isWielded() {
        return isWielded;
    }

    public void setWielded(boolean wielded) {
        isWielded = wielded;
    }

    public Location getInRoom() {
        return inRoom;
    }

    public void setInRoom(Location inRoom) {
        this.inRoom = inRoom;
    }

    public boolean isInRoom() {
        return isInRoom;
    }

    public void setIsInRoom(boolean inRoom) {
        isInRoom = inRoom;
    }

    public boolean isOpenable() {
        return isOpenable;
    }

    public void setOpenable(boolean openable) {
        isOpenable = openable;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public void setContainer(boolean container) {
        isContainer = container;
    }

    public boolean isNoGet() {
        return isNoGet;
    }

    public void setNoGet(boolean noGet) {
        isNoGet = noGet;
    }

    public boolean isExtinguish() {
        return isExtinguish;
    }

    public void setExtinguish(boolean extinguish) {
        isExtinguish = extinguish;
    }

    public boolean isLit() {
        return isLit;
    }

    public void setLit(boolean lit) {
        isLit = lit;
    }


    public boolean isWeapon() {
        return isWeapon;
    }

    public void setWeapon(boolean weapon) {
        isWeapon = weapon;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public void setInRoom(boolean inRoom) {
        isInRoom = inRoom;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isLockable() {
        return isLockable;
    }

    public void setLockable(boolean lockable) {
        isLockable = lockable;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }
}
