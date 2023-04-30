package Simplicity;

import Simplicity.Objects.*;
import Simplicity.Objects.Clock; // supaya ngga ambigu dengan Clock bawaan java
import Simplicity.House.*;


import java.time.*;
import java.util.*;

// TODO tambah syarat di Main bahwa sim harus hidup buat dipake
public class Sim {
    //Attributes
    private String simFullName;
    private Job simJob;
    private int simMoney;
    public Inventory simInventory;
    private int simHunger;
    private int simMood;
    private int simHealth;
    private String simStatus;
    private Point location;
    private House house;
    private Room curRoom;
    private World curWorld;

    //Attribute untuk ngecek kondisi
    private int activeDuration;
    private int lastSleep;
    private int lastBathroom;
    private int timeWorked;
    private int daysSinceJobChange;
    private int timeRemainingDelivery;
    private int timeRemainingUpgrade;
    private boolean hasEaten;
    private boolean isSimAlive;
    private boolean isItemInDelivery = false;
    private boolean isHouseBeingUpgraded = false;


    // get random job
    public Job getRandomJob(Job[] array){
        int random = new Random().nextInt(array.length);
        return array[random];
    }

    //Constructor
    public Sim(String fullName, Job[] jobList, World world){
        this.simFullName = fullName;
        this.simJob = getRandomJob(jobList);
        this.curWorld = world;
        simMoney = 100;
        simInventory = new Inventory();

        //setting starting numbers for sim needs
        simHunger = 80;
        simMood = 80;
        simHealth = 80;
        simStatus = "Idle";
        lastSleep = 0;
        lastBathroom = 0;
        timeWorked = 0;
        daysSinceJobChange = 0;
        hasEaten = false;
        isSimAlive = true;
        timeRemainingDelivery = 0;
        timeRemainingUpgrade = 0;
        isItemInDelivery = false;
        isHouseBeingUpgraded = false;
    }

    //Getters
    public String getSimName(){
        return simFullName;
    }

    public String getSimJobName(){
        return simJob.getName();
    }
    
    public int getActiveDuration(){
        return activeDuration;
    }

    public int getSimSalary(){
        return simJob.getSalary();
    }

    public int getSimMoney(){
        return simMoney;
    }

    public int getSimHunger(){
        return simHunger;
    }

    public int getSimMood(){
        return simMood;
    }

    public int getSimHealth(){
        return simHealth;
    }

    public String getSimStatus(){
        return simStatus;
    }

    public int getLastSleep(){
        return lastSleep;
    }

    public int getLastBathroom(){
        return lastBathroom;
    }

    public int getTimeWorked(){
        return timeWorked;
    }

    public int getLastJobChange(){
        return daysSinceJobChange;
    }

    public Point getLocation() {
        return location;
    }
    public int getTimeRemainingDelivery() { return timeRemainingDelivery;}
    public int getTimeRemainingUpgrade() { return timeRemainingUpgrade;}
    public boolean getSimAlive() { return isSimAlive;}
    public boolean getIsItemInDelivery() { return isItemInDelivery;}
    public boolean getIsHouseBeingUpgraded() { return isHouseBeingUpgraded;}
    // END OF GETTERS

    //Setters
    public void simChangeName(String name){
        simFullName = name;
        System.out.println("Your name is now: " + simFullName);
    }

    public void simChangeJob(Job job){
        // 12 * 60 = 12 menit dalam detik
        if (getTimeWorked() >= 30){
            // harus bisa bayar setengah gaji dari gaji pekerjaan baru
            if (getSimMoney() >= job.getSalary()/2){
                simJob = job;
                simDecreaseMoney(job.getSalary()/2);
                System.out.println("Success! Your new job is: " + job.getName());
                daysSinceJobChange = 0;
            }
            else{
                System.out.println("You don't have enough money to become a " + job.getName());
            }
        }
        else{
            System.out.println("You haven't worked long enough in your current job to change jobs");
        }

    }

    public void simAddMoney(int amount){
        simMoney += amount;
    }

    public void simDecreaseMoney(int amount){
        simMoney -= amount;
    }

    // ada exception buat angka negatif
    public void addSimNeed(String needType, int amount) throws negativeParameterException{
        if (amount < 0){
            throw new negativeParameterException(amount);
        }
        else{
            //menambahkan hunger
            switch (needType) {
                case "Hunger":
                    if (simHunger + amount >= 100) {
                        simHunger = 100;
                    } else {
                        simHunger += amount;
                    }
                    System.out.println("Your hunger is now " + simHunger);
                    break;

                //menambahkan Mood
                case "Mood":
                    if (simMood + amount >= 100) {
                        simMood = 100;
                    } else {
                        simMood += amount;
                    }
                    System.out.println("Your mood is now " + simMood);
                    break;

                //menambahkan Health
                case "Health":
                    if (simHealth + amount >= 100) {
                        simHealth = 100;
                    } else {
                        simHealth += amount;
                    }
                    System.out.println("Your health is now " + simHealth);
                    break;
            }
        }

    } // end of adding needs

    // ada exception buat angka negatif
    public void decreaseSimNeed(String needType, int amount) throws negativeParameterException{
        if (amount < 0){
            throw new negativeParameterException(amount);
        }
        else{
            //menambahkan hunger
            switch (needType) {
                case "Hunger":
                    if (simHunger - amount <= 0) {
                        simHunger = 0;
                    } else {
                        simHunger -= amount;
                    }
                    System.out.println("Your hunger is now " + simHunger);
                    break;

                //menambahkan Mood
                case "Mood":
                    if (simMood - amount <= 0) {
                        simMood = 0;
                    } else {
                        simMood -= amount;
                    }
                    System.out.println("Your mood is now " + simMood);
                    break;

                //menambahkan Health
                case "Health":
                    if (simHealth - amount == 0) {
                        simHealth = 0;
                    } else {
                        simHealth -= amount;
                    }
                    System.out.println("Your health is now " + simHealth);
                    break;
            }
        }

    } // end of decreasing needs


    //ganti status
    public void simChangeStatus(String newStatus){
        simStatus = newStatus;
    }

    public void changeLastBathroom(int amount){
        if (hasEaten){
            lastBathroom += amount;
        }
    }

    public void changeLastSleep(int amount){
        lastSleep += amount;
    }

    public void changeTimeWorked(int amount){
        timeWorked += amount;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void changeTimeDelivery(int amount){
        timeRemainingDelivery -= amount;
    }

    public void changeTimeUpgrade(int amount){
        timeRemainingUpgrade -= amount;
    }

    public void setActiveDuration(int amount){
        activeDuration = amount*1000;
    }
    // END OF SETTERS

    //Methods

    // Method untuk cek udh dapet gaji ato blm

    // Method untuk kerja
    public void work(int duration) throws invalidMultitudeNumber{
        // duration is in seconds
        if (duration % 120 != 0){
            throw new invalidMultitudeNumber(duration);
        }
        else{
            // Ganti status sim
            simChangeStatus("Working");
            // Mulai kerja
            System.out.println("You are currently working as a " + getSimJobName());
                    int repetition = duration / 30;
                    for (int i = 0; i<repetition; i++){
                        try{
                            setActiveDuration(duration);
                            // Biar keren gw bikin make '....'
                            for (int j = 0; j<30/6; j++){
                                System.out.print("...");
                                curWorld.getWorldTime().wait(30/5);
                            }
                            System.out.println("");
                            decreaseSimNeed("Mood", 10);
                            decreaseSimNeed("Hunger", 10);
                            //ganti jam kerja sim
                            changeTimeWorked(30);
                            // Ganti waktu gk ke toilet dll
                            changeLastBathroom(30);
                            changeLastSleep(30);
                            if(getTimeWorked() == 240){
                                simAddMoney(simJob.getSalary());
                                System.out.println("You have worked for 4 minutes.");
                                System.out.println("You earned " + simJob.getSalary() + " dollars.");
                                System.out.println("Your money: " + getSimMoney());
                            }
                            else{
                                System.out.println("You have worked for "+ (float)getTimeWorked()/60 + " minutes.");
                            }

                        }
                        catch (negativeParameterException n){
                            System.out.println(n.getMessage());
                        }
                    }
                }
            // check if sim died afterwards
            simDeathCheck();
            if (isSimAlive){
                simStatus = "Idle";
            }
        }

    //Method untuk olahraga
    public void exercise(int duration) throws invalidMultitudeNumber{
        if (duration % 20 != 0){
            throw new invalidMultitudeNumber(duration);
        }
        else{
            simChangeStatus("Exercising");
            //mulai olahraga
                    int repetition = duration / 20;
                    for (int i = 0; i<repetition; i++){
                        try{
                            System.out.println("Working out!");
                            // Biar keren gw bikin make '....'
                            for (int j = 0; j<20/4; j++){
                                System.out.print("...");
                                curWorld.getWorldTime().wait(20/5);
                            }
                            System.out.println("");
                            addSimNeed("Health", 5);
                            addSimNeed("Mood", 10);
                            decreaseSimNeed("Hunger", 5);
                            // ganti waktu gk tidur, gak ke kamar mandi, dll
                            changeLastSleep(20);
                            changeLastBathroom(20);
                        }
                        catch (negativeParameterException n){
                            System.out.println(n.getMessage());
                        }
                    }

                }
            simDeathCheck();
            if (isSimAlive){
                simStatus = "Idle";
            }
        }

    // Method untuk tidur
    public void sleep(int duration) throws invalidMultitudeNumber {
        if (duration % (4*60) != 0){
            throw new invalidMultitudeNumber(duration);
        }
        else{
            simChangeStatus("Sleeping");
                    int repetition = duration / (4*60);
                    for (int i = 0; i<repetition; i++){
                        try{
                            System.out.println("Zzzz...");
                            for (int j = 0; j<240/48; j++){
                                System.out.print("...");
                                curWorld.getWorldTime().wait(240/5);
                            }
                            System.out.println("");
                            addSimNeed("Mood", 30);
                            addSimNeed("Health", 20);
                            changeLastBathroom(240);
                            lastSleep = 0;
                        }
                        catch (negativeParameterException n){
                            System.out.println(n.getMessage());
                        }
                    }

                }
            simDeathCheck();
            if (isSimAlive){
                simStatus = "Idle";
            }
        }

    //Method untuk ke kamar mandi
    public void useBathroom(){
        simChangeStatus("In the bathroom");
                try{
                    System.out.println("Using the bathroom");
                    setActiveDuration(10);
                    for (int j = 0; j<10/2; j++){
                        System.out.print("...");
                        curWorld.getWorldTime().wait(10/5);
                    }
                    System.out.println("");
                    decreaseSimNeed("Hunger", 20);
                    addSimNeed("Mood", 10);
                    lastBathroom = 0;
                    hasEaten = false;
                    changeLastSleep(10);
                }
                catch (negativeParameterException n){
                    System.out.println(n.getMessage());
                }
        simDeathCheck();
        if (isSimAlive){
            simStatus = "Idle";
        }
        
    }

    // Method untuk Makan
    public void simEat(FoodCuisine food){
        if (simInventory.checkContains(food.getType())){
                    try{
                        System.out.println("Eating");
                        for (int j = 0; j<30/2; j++){
                            System.out.print("...");
                            curWorld.getWorldTime().wait(30/5);
                        }
                        System.out.println("");
                        addSimNeed("Hunger", food.getRepletion());
                        hasEaten = true;
                        changeLastSleep(30);
                        simInventory.decreaseInventory(food, 1);
                        // lastBathroom gadiubah karena baru kelar makan
                    }
                    catch (negativeParameterException n){
                        System.out.println(n.getMessage());
                    }

                }
        else{
            System.out.println("You don't have "+ food.getType() + " in your inventory");
        }
        simDeathCheck();
        if (isSimAlive){
            simStatus = "Idle";
        }
    }

    //Method untuk masak
    public void simCook(FoodCuisine food){
        // LOOPING buat cek udh punya ingredientsnya atau blm
        boolean allIngredients = true;
        List<FoodIngredients> listOfIngredieents = food.getIngredients();
        for (FoodIngredients ingredient: listOfIngredieents) {
            if (!simInventory.checkContains(ingredient.getType())) {
                allIngredients = false;
                break;
            }
        }
        // semua ingredient ada = masak gas
        if (allIngredients){
                    try{
                        System.out.println("Cooking");
                        for (int j = 0; j<((food.getRepletion()*1.5)/2); j++){
                            System.out.print("...");

                            //ERROR -> solved, cuman typo harusnya repletion bukan repletition
                            double sleepValDouble = (food.getRepletion()*1.5)/(food.getRepletion()%2) * 1000;
                            long sleepValLong = (long) sleepValDouble;
                            int sleepValInt = (int) sleepValDouble;
                            changeLastSleep(sleepValInt);
                            changeLastBathroom(sleepValInt);
                            curWorld.getWorldTime().wait(sleepValInt/1000);
                        }
                        System.out.println("");
                        addSimNeed("Mood", 10);
                        for (FoodIngredients ingredient: listOfIngredieents) {
                            simInventory.decreaseInventory(ingredient, 1);
                        }

                        //menambahkan makanan ke inventory
                        //ERROR -> solved
                        simInventory.addInventory(food, 1);
                    } catch (negativeParameterException n) {
                        System.out.println(n.getMessage());
                    }
            }
        else{
            System.out.println("You don't have all the ingredients, try again when you have them");
        }
        simDeathCheck();
        if (isSimAlive){
            simStatus = "Idle";
        }
        // cek status
    }

    public void simBuyItem(Map<String, PurchasableObject> objectMap,  String itemName, int amount) throws negativeParameterException, invalidMultitudeNumber{
        if (amount < 0){
            throw new negativeParameterException(amount);
        }
        else if(amount == 0){
            throw new invalidMultitudeNumber(amount); 
        }
        else{
            
            PurchasableObject object = objectMap.get(itemName);
            if (object != null){
                if (object.getPrice()*amount <= getSimMoney()){
                    Thread thread = new Thread(new Runnable(){
                        public void run(){
                            try{
                                if(!getIsItemInDelivery()){
                                    timeRemainingDelivery = new Random().nextInt(1,5)* 30 * 1000;
                                    isItemInDelivery = true;
                                    System.out.println("\nYou have bought " + ((SimplicityObject) object).getType() + " for " + object.getPrice() + " Simplicity Dollars.");   
                                    System.out.println("Please wait for "+ (float)timeRemainingDelivery/60000 +" minutes...");
                                    if(timeRemainingDelivery < activeDuration){
                                        Thread.sleep(timeRemainingDelivery);
                                        simDecreaseMoney(object.getPrice()*amount);
                                        simInventory.addInventory((SimplicityObject)object, amount);
                                        System.out.println("\nYour item has arrived!");
                                        System.out.println("You have " + getSimMoney() + " Simplicity Dollars left.");
                                        System.out.println();
                                        isItemInDelivery = false;
                                    }
                                    else{
                                        Thread.sleep(activeDuration);
                                        System.out.println("\nYou still have an item in deladsfadfivery, please wait until it arrives");
                                        timeRemainingDelivery -= activeDuration;
                                    }


                                }
                                else{
                                    System.out.println(timeRemainingDelivery);
                                    System.out.println(activeDuration);
                                    if(timeRemainingDelivery < activeDuration){
                                        Thread.sleep(timeRemainingDelivery);
                                        simDecreaseMoney(object.getPrice()*amount);
                                        simInventory.addInventory((SimplicityObject)object, amount);
                                        System.out.println("\nYour item has arrived!");
                                        System.out.println("You have " + getSimMoney() + " Simplicity Dollars left.");
                                        System.out.println();
                                        isItemInDelivery = false;
                                    }
                                    else{
                                        Thread.sleep(activeDuration);
                                        System.out.println("\nYou still have an item in delivery, please wait until it arrives");
                                        timeRemainingDelivery -= activeDuration;
                                    }
                                }
                            }
                            catch (InterruptedException e){
                                System.out.println(e.getMessage());
                            }
                        }
                    });
                    thread.start();
                }

                else{
                    System.out.println("You don't have enough money to buy " + ((SimplicityObject) object).getType());
                }
            }

            else{
                System.out.println("Item not found");
            }
        }
    }

    //method untuk berkunjung
    public void simVisit(House destination) {
        simChangeStatus("On the way to visit " + destination.toString());
        double distance = Math.sqrt(Math.pow(destination.getLocation().getX() - getLocation().getX(), 2)
                + Math.pow(destination.getLocation().getY() - getLocation().getY(), 2));
        int time = (int) distance;
            try {
                System.out.println("Visiting " + destination.toString());
                for (int j = 0; j < time; j++) {
                    System.out.print("...");
                    curWorld.getWorldTime().wait(1);
                    changeLastSleep(1);
                    changeLastBathroom(1);
                }
                System.out.println("");
                addSimNeed("Mood", 10);
                decreaseSimNeed("Hunger", 10);
            } 
            catch (negativeParameterException n) {
                System.out.println(n.getMessage());
            }
        simDeathCheck();
        if (isSimAlive){
            simStatus = "Idle";
        }
    }

    public void HouseUpgrade(int roomTotal, int duration) {
        int upgradeCost = 1500 * roomTotal;
        if (simMoney < upgradeCost) {
            System.out.println("Insufficient funds to upgrade the house.");
            return;
        }
        try {
            // Tunggu selama 18 menit
            Thread.sleep(18 * 60 * 1000);
            simDecreaseMoney(upgradeCost);

            // Tambahkan ruangan ke dalam house
            house.setRoomTotal(house.getRoomTotal() + roomTotal);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    public void simMoveRoom(){
        Scanner scanner = new Scanner(System.in);
        List<Room> roomList = house.getRoomList();

        String roomNow = curRoom.getRoomName(); //room tempat sim berada sekarang

        System.out.println("List of Rooms :"); // menampilkan daftar room yang ada di house
        for (Room room : roomList){
            System.out.println(room.getRoomName());
        }

        boolean check = false;
        while(!check){
            System.out.print("Insert your destination room : ");
            String roomName = scanner.nextLine();
            if (roomNow.equals(roomName)){
                System.out.println("You're already in the room");
                System.out.println();
            }else{
                // room yang di input ada di house
                for (Room room : roomList){
                    if (roomName.equals(room.getRoomName())){
                        Point roomLoc = new Point(room.getRoomLocation().getX(),room.getRoomLocation().getY());
                        setLocation(roomLoc); // sim pindah ke room tujuan
                        check = true;
                        break;
                    }
                }
                // room yang di input tidak ada di house
                if(!check){
                    System.out.println("There's no " + roomName + "in this house");
                    System.out.println();
                }
            }
        }
        scanner.close();
    }

    // Melihat waktu
    public void simCheckTime(Clock clock) {
        WorldTime worldTime = curWorld.getWorldTime();
        System.out.println("Time remaining today: " + worldTime.getTimeRemaining());
        System.out.println("Time remaining for upgrade house: " + timeRemainingUpgrade);
        System.out.println("Time remaining for item delivery: " + timeRemainingDelivery); // untuk tindakan yang bisa ditinggal
    }



    // Method untuk memeriksa kapan terakhir ke toilet
    public void checkLastBathroom(){
        if (lastBathroom >= 4 * 60){    //4*60 adalah 4 menit dalam detik
            System.out.println("You forgot to go to the bathroom after you ate!");
            try{
                decreaseSimNeed("Mood", 5);
                decreaseSimNeed("Health", 5);
            }
            catch (negativeParameterException n){
                System.out.println(n.getMessage());
            }
        }
    }

    // Method untuk memeriksa kapan terakhir tidur
    public void checkLastSleep(){
        if (lastBathroom >= 10 * 60){
            System.out.println("You haven't slept yet, please get some rest!");
            try{
                decreaseSimNeed("Mood", 5);
                decreaseSimNeed("Health", 5);
            }
            catch (negativeParameterException n){
                System.out.println(n.getMessage());
            }
        }
    }

    // Method untuk memeriksa kondisi overall SIM
    public void simDeathCheck(){
        if (simMood <= 0){
            System.out.println(simFullName + " is depressed. Your sim has died");
            deactivateSim();
        }
        else if (simHunger <= 0){
            System.out.println(simFullName + " has died of starvation");
            deactivateSim();
        }

        else if (simHealth == 0){
            System.out.println(simFullName + " has died due to poor health conditions");
            deactivateSim();
        }
    }

    // Method untuk menonaktifkan SIM
    public void deactivateSim(){
        isSimAlive = false;
    }

    //Method untuk pengecekan dan pelaksanaan tindakan yang bisa ditinggal

    // TODO yang implementasi waktu bikin perhitungan waktu per hari dan pergantian hari


    // END OF METHODS

    // Diimplementasikan dengan hash map
    public class Inventory{
        HashMap<SimplicityObject, Integer> MapInventory;

        public Inventory(){
            MapInventory = new HashMap<SimplicityObject, Integer>();
        }

        public void addInventory(SimplicityObject item, Integer itemAmount){
            if (MapInventory.containsKey(item)){
                MapInventory.replace(item, (MapInventory.get(item)+itemAmount));
                System.out.println("Added " + itemAmount + " " + item.getType() + "(s) to your inventory");
            }
            else{
                MapInventory.putIfAbsent(item, itemAmount);
                System.out.println(item.getType()+ " is now in your inventory" + " (" + itemAmount + ")");
            }
        }

        public void decreaseInventory(SimplicityObject item, int itemAmount){
            if (MapInventory.containsKey(item) && (MapInventory.get(item)-itemAmount >= 1)){
                MapInventory.replace(item, (MapInventory.get(item)-itemAmount));
            }
            else if (MapInventory.get(item)-itemAmount <= 0){
                MapInventory.remove(item);
            }
            else{
                System.out.println("The item "+ item.getType()+ " is not in your inventory!");
            }
        }

        public boolean checkContains(String objName){
            boolean isContained = false;
            for (Map.Entry<SimplicityObject, Integer> entry: MapInventory.entrySet()){
                if (entry.getKey().getType().equals(objName)){
                    isContained = true;
                    break;
                }
            }
            return isContained;
        }

        public int getNumberOfItems(){
            return MapInventory.size();
        }

        public void printInventory(){
            if (!MapInventory.isEmpty()){
                System.out.println(getSimName() + "'s Inventory: ");
                for (Map.Entry<SimplicityObject, Integer> entry: MapInventory.entrySet()){
                    System.out.println(entry.getKey().getType() + ", amount: " + entry.getValue());
                }
            }

            else{
                System.out.println("Your inventory is empty");
            }
        }

    }


}

