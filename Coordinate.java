public class Coordinate {
    private int[][] coordinate = new int[5][5];
    Ship ship1; //A4
    Ship ship2; //B2
    Ship ship3; //D4
    Ship ship4; //E2

    private int masCount = 0; //使えなくなったマスの数
    private int dieCount = 0; //自軍の撃沈した艦の数
    private int xx, yy; //操作用座標

    private Ship ship;

    Coordinate(){
        for(int i = 0; i < 5; i++){
            for(int j  = 0; j < 5; j++){
                coordinate[j][i] = 0;
            }
        }
        int n = (int)(Math.random() * 10);
        //initCoordinate(n); //決められた強そうな初期座標１０種から選ぶ
        initCordRandom();  //ランダムに４隻艦を配置する

    }

    public void initCoordinate(int n){
        if(n==1){
            ship1 = new Ship(3,0); //A4
            coordinate[0][3] = 1;
            ship2 = new Ship(1,1); //B2
            coordinate[1][1] = 1;
            ship3 = new Ship(3,3); //D4
            coordinate[3][3] = 1;
            ship4 = new Ship(1,4); //E2
            coordinate[4][1] = 1;
        }else if(n==2){
            ship1 = new Ship(3,1); //B4
            coordinate[1][3] = 1;
            ship2 = new Ship(1,0); //A2
            coordinate[0][1] = 1;
            ship3 = new Ship(3,4); //E4
            coordinate[4][3] = 1;
            ship4 = new Ship(1,3); //D2
            coordinate[3][1] = 1;
        }else if(n==3){
            ship1 = new Ship(0,3); //D1
            coordinate[3][0] = 1;
            ship2 = new Ship(1,1); //B2
            coordinate[1][1] = 1;
            ship3 = new Ship(3,3); //D4
            coordinate[3][3] = 1;
            ship4 = new Ship(4,1); //B5
            coordinate[1][4] = 1;
        }else if(n==4){
            ship1 = new Ship(3,1); //B4
            coordinate[1][3] = 1;
            ship2 = new Ship(0,1); //B1
            coordinate[1][0] = 1;
            ship3 = new Ship(4,3); //D5
            coordinate[3][4] = 1;
            ship4 = new Ship(1,3); //D2
            coordinate[3][1] = 1;
        }else if(n==5){
            ship1 = new Ship(1,0); //A2
            coordinate[0][1] = 1;
            ship2 = new Ship(0,3); //D1
            coordinate[3][0] = 1;
            ship3 = new Ship(4,1); //B5
            coordinate[1][4] = 1;
            ship4 = new Ship(3,4); //E4
            coordinate[4][3] = 1;
        }else if(n==6){
            ship1 = new Ship(0,1); //B1
            coordinate[1][0] = 1;
            ship2 = new Ship(3,0); //A4
            coordinate[0][3] = 1;
            ship3 = new Ship(1,4); //E2
            coordinate[4][1] = 1;
            ship4 = new Ship(4,3); //D5
            coordinate[3][4] = 1;
        }else if(n==7){
            ship1 = new Ship(1,1); //B2
            coordinate[1][1] = 1;
            ship2 = new Ship(0,3); //D1
            coordinate[3][0] = 1;
            ship3 = new Ship(3,4); //E4
            coordinate[4][3] = 1;
            ship4 = new Ship(4,0); //A5
            coordinate[0][4] = 1;
        }else if(n==8){
            ship1 = new Ship(1,1); //B2
            coordinate[1][1] = 1;
            ship2 = new Ship(3,0); //A4
            coordinate[0][3] = 1;
            ship3 = new Ship(0,4); //E1
            coordinate[4][0] = 1;
            ship4 = new Ship(4,3); //D5
            coordinate[3][4] = 1;
        }else if(n==9){
            ship1 = new Ship(1,0); //A2
            coordinate[0][1] = 1;
            ship2 = new Ship(4,1); //B5
            coordinate[1][4] = 1;
            ship3 = new Ship(3,3); //D4
            coordinate[3][3] = 1;
            ship4 = new Ship(0,4); //E1
            coordinate[4][0] = 1;
        }else{
            ship1 = new Ship(0,0); //A1
            coordinate[0][0] = 1;
            ship2 = new Ship(1,4); //E2
            coordinate[4][1] = 1;
            ship3 = new Ship(3,1); //B4
            coordinate[1][3] = 1;
            ship4 = new Ship(4,3); //D5
            coordinate[3][4] = 1;
        }
    }

    public void initCordRandom(){
        int count = 0;
        while(count < 3){
            int x = (int)(Math.random() * 5);
            int y = (int)(Math.random() * 5);
            if(x == 2 && y == 1){
                //何もしない
            }else{
                if(getCoordinate(x,y) == 0 && count == 0){
                    ship1 = new Ship(x,y);
                    coordinate[y][x] = 1;
                    count++;
                }else if(getCoordinate(x,y) == 0 && count == 1){
                    ship2 = new Ship(x,y);
                    coordinate[y][x] = 1;
                    count++;
                }else if(getCoordinate(x,y) == 0 && count == 2){
                    ship3 = new Ship(x,y);
                    coordinate[y][x] = 1;
                    count++;
                }
            }
        }
        if(existarround(2, 1)){
            while(count == 3){
                int x = (int)(Math.random() * 5);
                int y = (int)(Math.random() * 5);
                if(x == 2 && y == 1){
                    //何もしない
                }else if(getCoordinate(x,y) == 0){
                    ship4 = new Ship(x,y);
                    coordinate[y][x] = 1;
                    count++;
                }
            }
        }else{
            while(count == 3){
                int x = (int)(Math.random() * 5);
                int y = (int)(Math.random() * 5);
                if(x == 2 && y == 1){
                    //何もしない
                }else if(getCoordinate(x,y) == 0 && 1 <= x && x <= 3 && 0 <= y && y <= 2){
                    ship4 = new Ship(x,y);
                    coordinate[y][x] = 1;
                    count++;
                }
            }
        }
    }

    public int getCoordinate(int x, int y){
        return coordinate[y][x];
    }

    public int enemyAttack(int x, int y){
        if(coordinate[y][x] == 1){
            System.out.print("命中!");
            return 0;
        }else if(namiJudge(x, y)){
            System.out.println("波高!");
            return 1;
        }else{
            System.out.println("ハズレ!");
            return 2;
        }
    }

    public int damageMe(int x, int y){
        if(ship1.getShip(x, y)){
            if(ship1.getDamage()){
                sink(ship1.getShipc());
                masCount += 1;
                dieCount += 1;
                return 10;
            }
            return 1;
        }else if(ship2.getShip(x, y)){
            if(ship2.getDamage()){
                sink(ship2.getShipc());
                masCount += 1;
                dieCount += 1;
                return 20;
            }
            return 2;
        }else if(ship3.getShip(x, y)){
            if(ship3.getDamage()){
                sink(ship3.getShipc());
                masCount += 1;
                dieCount += 1;
                return 30;
            }
            return 3;
        }else if(ship4.getShip(x, y)){
            if(ship4.getDamage()){
                sink(ship4.getShipc());
                masCount += 1;
                dieCount += 1;
                return 40;
            }
            return 4;
        }
        return 0;
    }

    public boolean namiJudge(int x, int y){
        if(x == 0 && y == 0){
            if(getCoordinate(0, 1) == 1 || getCoordinate(1, 0) == 1 || getCoordinate(1, 1) == 1){
                return true;
            }else{
                return false;
            }
        }else if(x == 4 && y == 4){
            if(getCoordinate(3, 3) == 1 || getCoordinate(3, 4) == 1 || getCoordinate(4, 3) == 1){
                return true;
            }else{
                return false;
            }
        }else if(x == 0 && y == 4){
            if(getCoordinate(0, 3) == 1 || getCoordinate(1, 3) == 1 || getCoordinate(1, 4) == 1){
                return true;
            }else{
                return false;
            }
        }else if(x == 4 && y == 0){
            if(getCoordinate(3, 0) == 1 || getCoordinate(3, 1) == 1 || getCoordinate(4, 1) == 1){
                return true;
            }else{
                return false;
            }
        }else if(x == 0 && 0 < y && y < 4){
            if(getCoordinate(0, y - 1) == 1 || getCoordinate(0, y + 1) == 1 || getCoordinate(1, y - 1) == 1 || getCoordinate(1, y) == 1 || getCoordinate(1, y + 1) == 1){
                return true;
            }else{
                return false;
            }
        }else if(x == 4 && 0 < y && y < 4){
            if(getCoordinate(4, y - 1) == 1 || getCoordinate(4, y + 1) == 1 || getCoordinate(3, y - 1) == 1 || getCoordinate(3, y) == 1 || getCoordinate(3, y + 1) == 1){
                return true;
            }else{
                return false;
            }
        }else if(0 < x && x < 4 && y == 0){
            if(getCoordinate(x - 1, 0) == 1 || getCoordinate(x + 1, 0) == 1 || getCoordinate(x - 1, 1) == 1 || getCoordinate(x, 1) == 1 || getCoordinate(x + 1, 1) == 1){
                return true;
            }else{
                return false;
            }
        }else if(0 < x && x < 4 && y == 4){
            if(getCoordinate(x - 1, 4) == 1 || getCoordinate(x + 1, 4) == 1 || getCoordinate(x - 1, 3) == 1 || getCoordinate(x, 3) == 1 || getCoordinate(x + 1, 3) == 1){
                return true;
            }else{
                return false;
            }
        }else{ //中９マスの波高判定
            if(getCoordinate(x - 1, y - 1) == 1 || getCoordinate(x - 1, y) == 1 || getCoordinate(x - 1, y + 1) == 1 || getCoordinate(x, y -1) == 1 || getCoordinate(x, y + 1) == 1){
                return true;
            }else if(getCoordinate(x + 1, y - 1) == 1 || getCoordinate(x + 1, y) == 1 || getCoordinate(x + 1, y + 1) == 1){
                return true;
            }else{
                return false;
            }
        }
    }

    public void sink(int i){
        int a = i;
        int y = a / 10;
        int x = a % 10;
        coordinate[y][x] = -1;
        ship1.setShip(x, y, -1);
        ship2.setShip(x, y, -1);
        ship3.setShip(x, y, -1);
        ship4.setShip(x, y, -1);
    }

    public boolean losejudge(){
        if(dieCount == 4) return true;
        else              return false;
    }

    public boolean move(int z1, int z2){
        //System.out.println("move");
        if(z1 == -1 || z2 == -1) return false;
        //移動前座標
        int a1 = z1;
        int y1 = a1 / 10;
        int x1 = a1 % 10;
        //移動後座標
        int a2 = z2;
        int y2 = a2 / 10;
        int x2 = a2 % 10;

        if(ship1.getShip(x1, y1)){
            if(coordinate[y2][x2] == 0){
                setCoordinate(x1, y1, 0);
                setCoordinate(x2, y2, 1);
                ship1.setShip(x1, y1, 0);
                ship1.setShip(x2, y2, 1);
                return true;
            }
        }else if(ship2.getShip(x1, y1)){
            if(coordinate[y2][x2] == 0){
                setCoordinate(x1, y1, 0);
                setCoordinate(x2, y2, 1);
                ship2.setShip(x1, y1, 0);
                ship2.setShip(x2, y2, 1);
                return true;
            }
        }else if(ship3.getShip(x1, y1)){
            if(coordinate[y2][x2] == 0){
                setCoordinate(x1, y1, 0);
                setCoordinate(x2, y2, 1);
                ship3.setShip(x1, y1, 0);
                ship3.setShip(x2, y2, 1);
                return true;
            }
        }else if(ship4.getShip(x1, y1)){
            if(coordinate[y2][x2] == 0){
                setCoordinate(x1, y1, 0);
                setCoordinate(x2, y2, 1);
                ship4.setShip(x1, y1, 0);
                ship4.setShip(x2, y2, 1);
                return true;
            }
        }
        return false;
    }

    public void setCoordinate(int x, int y, int n){
        coordinate[y][x] = n;
    }

    public boolean existarround(int x, int y){
        for(int j=y-1; j<y+2; j++){
            for(int i=x-1; i<x+2; i++){
                if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(getCoordinate(i, j) == 1){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int whichShip(int z){
        int a = z;
        int y = a / 10;
        int x = a % 10;
        if(ship1.getShip(x, y)){
            return 1;
        }else if(ship2.getShip(x, y)){
            return 2;
        }else if(ship3.getShip(x, y)){
            return 3;
        }else if(ship4.getShip(x, y)){
            return 4;
        }else{
            return 0;
        }
    }

    public void kakuninCO(){ //デバッグ用
        System.out.println("\n<kakuninCO>");
        int n = 0;
        double max = -1;
        for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                if(coordinate[j][i] == -1) System.out.print(coordinate[j][i]+" ");
                else System.out.print(" "+coordinate[j][i]+" ");
            }
            System.out.println();
        }
    }
}
