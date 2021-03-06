package me.thehandsomeyoni.persistentdataapi.manager;

import me.thehandsomeyoni.persistentdataapi.AbstractPersistentData;
import me.thehandsomeyoni.persistentdataapi.data.PersistentData;
import me.thehandsomeyoni.persistentdataapi.events.ItemPersistentDataDeleteEvent;
import me.thehandsomeyoni.persistentdataapi.events.ItemPersistentDataWriteEvent;
import me.thehandsomeyoni.persistentdataapi.events.PlayerPersistentDataDeleteEvent;
import me.thehandsomeyoni.persistentdataapi.events.PlayerPersistentDataWriteEvent;
import me.thehandsomeyoni.persistentdataapi.exceptions.DataException;
import me.thehandsomeyoni.persistentdataapi.data.DataContainerManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import static me.thehandsomeyoni.persistentdataapi.PersistentDataAPI.getJavaPlugin;

/**
 * Handles all persistent data registry operations.
 * @author TheHandsomeYoni
 * @since 1.0
 */
public class DataRegistry {
    private Player player;
    private ItemStack item;
    private PersistentDataType dataType = PersistentDataType.BYTE_ARRAY;
    private ItemMeta meta;
    private PersistentDataContainer dataContainer;
    private DataContainerManager manager;

    /**
     * Initializes the DataRegistry of a player.
     * @param player The player that the data is stored in.
     */
    public DataRegistry(Player player){
        this.player = player;
        this.dataContainer = player.getPersistentDataContainer();
        this.manager = new DataContainerManager(dataContainer);
    }

    /**
     * Initializes the DataRegistry of an item.
     * @param item The item that the data is stored in.
     */
    public DataRegistry(ItemStack item){
        this.item = item;
        this.meta = item.getItemMeta();
        this.dataContainer = meta.getPersistentDataContainer();
        this.manager = new DataContainerManager(dataContainer);
    }

    /**
     * Registers a new data with the given name and type.
     * @param dataName the name of the data.
     * @param dataValue the value of the data.
     * @throws DataException if the data already exists.
     */
    public void registerData(String dataName, Serializable dataValue) throws DataException {
        if(manager.has(dataName)) throw new DataException("Data already exists!");

        manager.setSerialized(dataName, dataValue);

        if(player != null){
            PlayerPersistentDataWriteEvent event = new PlayerPersistentDataWriteEvent(player, new PersistentData(dataName, dataValue));
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataWriteEvent event = new ItemPersistentDataWriteEvent(item, new PersistentData(dataName, dataValue));
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Registers a new data from a class that extends AbstractPersistentData.
     * @param data The data that is being registered. This data must extend AbstractPersistentData.
     * @throws DataException if the data already exists.
     */
    public void registerData(AbstractPersistentData data) throws DataException {
        if(manager.has(data.getDataName())) throw new DataException("Data already exists!");

        manager.setSerialized(data.getDataName(), data.getDataValue());

        if(player != null){
            PlayerPersistentDataWriteEvent event = new PlayerPersistentDataWriteEvent(player, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataWriteEvent event = new ItemPersistentDataWriteEvent(item, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Removes a data from the data container.
     * @param dataName The name of the data that is being removed.
     */
    public void unregisterData(String dataName) throws DataException {
        if(!manager.has(dataName)) throw new DataException("Data doesn't exist!");

        manager.remove(dataName);

        if(player != null){
            PlayerPersistentDataDeleteEvent event = new PlayerPersistentDataDeleteEvent(player, dataName);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataDeleteEvent event = new ItemPersistentDataDeleteEvent(item, dataName);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Removes a data from the data container.
     * @param data The data that is being removed.
     */
    public void unregisterData(AbstractPersistentData data) throws DataException {
        if(!manager.has(data.getDataName())) throw new DataException("Data doesn't exist!");

        manager.remove(data.getDataName());

        if(player != null){
            PlayerPersistentDataDeleteEvent event = new PlayerPersistentDataDeleteEvent(player, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataDeleteEvent event = new ItemPersistentDataDeleteEvent(item, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }


    /**
     * Changes the values of a data.
     * @param data the data that is being changed.
     */
    public void setData(AbstractPersistentData data) {
        manager.setSerialized(data.getDataName(), data.getDataValue());

        if(player != null){
            PlayerPersistentDataWriteEvent event = new PlayerPersistentDataWriteEvent(player, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataWriteEvent event = new ItemPersistentDataWriteEvent(item, data);
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Changes the values of a data.
     * @param dataName name of the data that is being changed.
     * @param dataValue the new value of the data.
     */
    public void setData(String dataName, Serializable dataValue) {
        manager.setSerialized(dataName, dataValue);

        if(player != null){
            PlayerPersistentDataWriteEvent event = new PlayerPersistentDataWriteEvent(player, new PersistentData(dataName, dataValue));
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }

        if (item != null) {
            ItemPersistentDataWriteEvent event = new ItemPersistentDataWriteEvent(item, new PersistentData(dataName, dataValue));
            getJavaPlugin().getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Gets the value of a data.
     * @param dataName the name of the data.
     * @return the value of the data.
     * @throws DataException if the data doesn't exist.
     */
    public Serializable getData(String dataName) throws DataException {
        if(!manager.has(dataName)) throw new DataException("Data doesn't exist!");

        return manager.getUnserialized(dataName);
    }

    /**
     * Gets the value of a data.
     * @param data the data that is being gotten.
     * @return the value of the data.
     * @throws DataException if the data doesn't exist.
     */
    public Serializable getData(AbstractPersistentData data) throws DataException {
        if(!manager.has(data.getDataName())) throw new DataException("Data doesn't exist!");

        return manager.getUnserialized(data.getDataName());
    }

    public HashMap<String, Serializable> getAllData() {
        return manager.getAll();
    }

    public Set<String> getKeys(){
        return manager.getAllKeys();
    }

    /**
     * Gets the value of a data as its primitive type.
     * @param dataName the name of the data.
     * @return the value of the data in bytes.
     * @throws DataException if the data doesn't exist.
     */
    public byte[] getDataAsBytes(String dataName) throws DataException {
        if(!manager.has(dataName)) throw new DataException("Data doesn't exist!");

        return manager.getSerialized(dataName);
    }

    /**
     * Gets the value of a data as its primitive type.
     * @param data the data that is being gotten.
     * @return the value of the data in bytes.
     * @throws DataException if the data doesn't exist.
     */
    public byte[] getDataAsBytes(AbstractPersistentData data) throws DataException {
        if(!manager.has(data.getDataName())) throw new DataException("Data doesn't exist!");

        return manager.getSerialized(data.getDataName());
    }

    /**
     * Checks if a data exists.
     * @param dataName the name of the data.
     * @return true if the data exists, false if it doesn't.
     */
    public boolean dataExists(String dataName) {
        return manager.has(dataName);
    }

    /**
     * Checks if a data exists.
     * @param data the data that is being checked.
     * @return true if the data exists, false if it doesn't.
     */
    public boolean dataExists(AbstractPersistentData data) {
        return manager.has(data.getDataName());
    }
}
