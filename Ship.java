public class Ship {
    private int[][] ship = new int[5][5];
    private int HP;

    Ship(int x, int y){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                this.ship[i][j] = 0;
            }
        }
        this.ship[y][x] = 1;
        HP = 3;
    }

    public void setShip(int x, int y, int n){
        ship[y][x] = n;
    }

    public boolean getShip(int x, int y){
        if(ship[y][x] == 1){
            return true;
        }else{
            return false;
        }
    }

    public int getHP(){
        return this.HP;
    }

    public int getShipc(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(this.ship[j][i] == 1){
                    return j * 10 + i;
                }
            }
        }
        return -1;
    }

    public boolean getDamage(){
        this.HP --;
        System.out.print(" HP: " + (this.HP + 1) + " --> " + this.HP);

        if(this.HP == 0){
            System.out.println(" 撃沈！\n");
            return true;
        }
        System.out.print("\n");
        return false;
    }
}
