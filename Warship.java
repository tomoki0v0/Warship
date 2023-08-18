//検索窓
import java.util.Scanner;

public class Warship {
    public Coordinate coordinate;
    public ToEnemy toEnemy;
    public EnemyPredict enemyPredict;

    int mode = 0; //評価値一覧、移動座標の出力をするか否か　0:しない 1:する

    int choice, x, y;
    int hant; //命中0 波高1 ハズレ2(敵からの攻撃)
    int HANT1, HANT2; //命中0 波高1 ハズレ2　(自軍の攻撃)
    int xx, yy; // 砲撃座標
    int direction; //移動方向　東 0 西 1 南 2 北 3
    int goMasu;
    int miss;
    int turn = 1;
    int h = 0; //判定用変数
    int bluff = 0; //ブラフするかどうかで使う変数
    int action = 0; //敵が攻撃したら(1)にする　敵が移動したらリセット(0)
    int act = 0; //敵が移動したらリセット actionが1の間加算していく

    int killCount = 0;

    Scanner sc = new Scanner(System.in);

    public static void main(String args[]){
        new Warship().execute();
    }

    public void execute(){
        initialize();
        int sengo;
        while(true){
            System.out.print("デバッグ出力 しない 0 する 1: ");
            String strin = sc.next();
            if(strin.length() == 1){
                if(isNumber(strin)){
                    mode = Integer.parseInt(strin);
                    if(mode == 0 || mode == 1){
                        break;
                    }else{
                        System.out.println("0 か 1 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }
        System.out.println("\n< Warship Start >");
        if(mode != 0){
            coordinate.kakuninCO();
            System.out.println();
        }
        while(true){
            System.out.print("先手 0 後手 1: ");
            String strin = sc.next();
            if(strin.length() == 1){
                if(isNumber(strin)){
                    sengo = Integer.parseInt(strin);
                    if(sengo == 0 || sengo == 1){
                        break;
                    }else{
                        System.out.println("0 か 1 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }
        if(sengo == 0){ //先攻の場合
            System.out.println("\n-------myturn " + turn + " ------");
            syori(12);
        }
        while(true){
            System.out.println("\n-----enemyturn " + turn + " -----");
            enemyTurn(); //相手のターン
            if(sengo == 0){
                if(mode != 0) coordinate.kakuninCO();
                turn++;
                System.out.println();
            }
            System.out.println("\n-------myturn " + turn + " ------");
            myTurn(); //自分のターン
            if(sengo == 1){
                if(mode != 0) coordinate.kakuninCO();
                turn++;
                System.out.println();
            }
        }
    }

    public void initialize(){
        coordinate = new Coordinate();
        toEnemy = new ToEnemy();
        enemyPredict = new EnemyPredict();
        kousin();
    }

    public void kousin(){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                enemyPredict.setCoordinateEP(i, j, coordinate.getCoordinate(i, j));
                toEnemy.setCoordinateTE(i, j, coordinate.getCoordinate(i, j));
            }
        }
        toEnemy.junjokousinTE();
        enemyPredict.shipjunjo();
    }

    public void kakunin(){
        toEnemy.kakuninTE();
        enemyPredict.kakuninEP();
        coordinate.kakuninCO();
        System.out.println();
    }

    public int quantification(char c){ //アルファベットを数に変換
        if(c == 'a' || c == 'A'){
            return 0;
        }else if(c == 'b' || c == 'B'){
            return 1;
        }else if(c == 'c' || c == 'C'){
            return 2;
        }else if(c == 'd' || c == 'D'){
            return 3;
        }else if(c == 'e' || c == 'E'){
            return 4;
        }else{
            return -1;
        }
    }

    public void choose(){
        while(true){

            System.out.print("移動 0 or 砲撃 1 or 評価値出力 7: ");
            String str = sc.next();
            if(str.length() == 1){
                if(isNumber(str)){
                    choice = Integer.parseInt(str);
                    if(choice == 0 || choice == 1){
                        break;
                    }else if(choice == 7){
                        kakunin();
                        System.out.println("\n-----enemyturn " + turn + " -----");
                    }else{
                        System.out.println("0 か 1 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }
    }

    public boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void moving(){ //敵の移動の時に使用
        while(true){
            System.out.print("東 0 西 1 南 2 北 3: ");
            String r = sc.next();
            if(r.length() == 1){
                if(isNumber(r)){
                    direction = Integer.parseInt(r);
                    if(direction == 0 || direction == 1 || direction == 2 || direction == 3){
                        break;
                    }else{
                        System.out.println("0 か 1 か 2 か 3 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }
        while(true){
            System.out.print("何マス: ");
            String st = sc.next();
            if(st.length() == 1){
                if(isNumber(st)){
                    goMasu = Integer.parseInt(st);
                    if(goMasu == 1 || goMasu == 2){
                        break;
                    }else{
                        System.out.println("1 か 2 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }

    }

    public void getting(){
        while(true){
            int i = 0;
            System.out.print("砲撃座標: ");
            String s = sc.next();
            if(s.length() == 2){
                char alfa = s.charAt(0);
                y = quantification(alfa);
                if(y == 0 || y == 1 || y == 2 || y == 3 || y == 4){
                    i += 1;
                }

                x = s.charAt(1) - 49;
                if(x == 0 || x == 1 || x == 2 || x == 3 || x == 4){
                    i += 1;
                }

                if(i == 2){
                    break;
                }else{
                    System.out.println("入力が不正です");
                }
            }else{
                System.out.println("文字列の長さが2ではありません");
            }

        }
    }

    public void enemyTurn(){
        kousin();
        choose(); // 敵が移動か砲撃かを判定 0移動 1砲撃
        if(choice == 0){//敵が移動
            moving();
            action = 0;
        }else{//敵が砲撃
            getting(); // 座標の取得
            if(action == 1) act++;
            action = 1;
        }

        if(choice == 0){ //敵が移動　つづき
            System.out.println("\n敵が " + hougaku(direction) + " 方向に " + goMasu + " マス移動した！");
            if(HANT1 == 0 && toEnemy.enemycanmove(direction, goMasu)){
                if(toEnemy.runenemy(direction, goMasu)){ //前のターンで敵に命中したとき
                }else toEnemy.notrunenemy();
            }else{
                toEnemy.moveenemy(direction, goMasu);
            }
            hant = 3;
        }else{ //敵が砲撃　つづき
            System.out.println("\n敵が座標(" + x + ", " + y + ")に砲撃した！");
            hant = coordinate.enemyAttack(x, y);
            toEnemy.attackenemyTE(x, y);
            if(hant == 0){ //敵の攻撃　命中
                damageMe(x, y);
                //toEnemy.teHit(x, y);
                if(h != 1){ //命中はされたが、撃沈はしていないとき
                    enemyPredict.epHit(x, y);
                }else{ //敵の攻撃によって撃沈したとき
                    h = 0;
                    hant = 3;
                }
                if(coordinate.losejudge()){ //負け判定
                    System.out.println("You lose...\n");
                    System.exit(0);
                }
            }else if(hant == 1){ //敵の攻撃　波高
                toEnemy.teNamitaka(x, y);
                enemyPredict.epNamitaka(x, y);
            }else{ //敵の攻撃　外れ
                toEnemy.teHazure(x, y);
                enemyPredict.epHazure(x, y);
            }
        }
    }

    public void myTurn(){
    	int cando = 0;
    	int movemove = 0;
    	kousin();

        if(hant == 0){
            move();
            cando = 1;
        }

    	for(int i = 0; i < 5; i++){
    		for(int j =0; j < 5; j++){
    			if(enemyPredict.getEnemyPredict(i, j) >= 30 && coordinate.getCoordinate(i, j) == 1){
    				movemove = 1;
    			}
    		}
    	}
    	if(movemove == 1 && cando == 0){ //移動
    		move();
    		cando = 1;
    	}else if(cando == 0){ //砲撃
    		int cycle = 0;
    		int k = 0;
    		int maxCount; //maxが複数のカウント
    		int[][] confirm = new int[5][5]; //確認用の配列
    		for(int i = 0; i < 5; i++){
    			for(int j = 0; j < 5; j++){
    				confirm[y][x] = 0; //初期化
    			}
    		}

    		out:while(cycle <= 8 && k <= 7){
                System.out.println("myturn out:while");
    			maxCount = 0;
    			for(int i = 0; i < 5; i++){
    				for(int j = 0; j < 5; j++){
    					if(toEnemy.getmax(k) == toEnemy.getToEnemy(i, j)){
    						maxCount++; //Maxを数える
    					}
    				}
                }
    			middle:while(0 < maxCount){
                    System.out.println("myturn middle:while");
    				if(maxCount > 1){
    					in:while(maxCount > 1){
                            System.out.println("myturn in:while");
    						int ii;
    						int jj;
    						ii = (int)(Math.random() * 5);
    						jj = (int)(Math.random() * 5);
    						if(confirm[jj][ii] == 0){
    							if(toEnemy.getmax(k) == toEnemy.getToEnemy(ii,jj)){
    								if(canAttack(ii, jj)){
    									syori(jj*10+ii);
    									cando = 2;
    									break out;
    								}else{
    									maxCount--;
    									cycle++;
    									confirm[jj][ii] = 1;
    									break in;
    								}
    							}else{
                                    confirm[jj][ii] = 1;
                                }
    						}
    					}//while in
    				}else if(maxCount == 1){
    					for(int i = 0; i < 5; i++){
    						for(int j = 0; j < 5; j++){
    							if(confirm[j][i] == 0 && toEnemy.getToEnemy(i, j) == toEnemy.getmax(k)){
    								if(canAttack(i, j)){
    									syori(j*10+i);
    									cando = 2;
    									break out;
    								}else{
    									maxCount--;
    									cycle++;
    									k++;
    								}
    							}
    						}
    					}
    				}
    			}//while middle
    		}//while out
    	}

    	if(cando == 0){ //強制移動
    		move();
    		cando = 1;
    	}

    	if(cando == 0){
    		System.out.println("動作を完了していません。例外が発生しました。");
    		System.exit(0);
    	}else if(cando == 2){
            if(mode != 0) System.out.println("行動：通常攻撃");
        }
    }

    public void move(){
        int n = -1;
        int cycle2 = 0;
        out:while(cycle2 < 10){
            System.out.println("move out:while");
            int cycle1 = 0;
            int k = 0;
            int[][] confirm = new int[5][5]; //確認用の配列
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    confirm[y][x] = 0; //初期化
                }
            }

            in:while(k < 4 && cycle1 < 30){
                System.out.println("move in:while");
                n = -1;
                int mv1 = -1;
                int mv2 = -1;
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        if(confirm[j][i] == 0){
                            if(canmove(i, j) && enemyPredict.getshipjunjo(k) == enemyPredict.getEnemyPredict(i, j) && coordinate.getCoordinate(i, j) == 1){
                                mv1 = j * 10 + i;
                            }
                        }
                    }
                }
                if(mv1 != -1){
                    if(hant == 0){
                        if(Math.random() < 1.0){ //ブラフ
                            mv1 = enemyPredict.bluff1(mv1);
                            mv2 = enemyPredict.idounosyori(mv1);
                            n = 2;
                        }else{ //逃亡
                            mv2 = enemyPredict.idounosyori2(mv1);
                            n = 1;
                        }
                    }else{ //通常移動
                        mv2 = enemyPredict.idounosyori2(mv1);
                        n = 0;
                    }
                    if(coordinate.move(mv1, mv2)){
                        ugokisyuturyoku(mv1, mv2);
                        if(n == 2){
                            enemyPredict.runme(mv1, mv2);
                            if(mode != 0) System.out.println("行動：ブラフ");
                        }else if(n == 1){
                            enemyPredict.runme(mv1, mv2);
                            if(mode != 0) System.out.println("行動：逃亡");
                        }else{
                            enemyPredict.moveme(mv1, mv2);
                            if(mode != 0) System.out.println("行動：通常移動");
                        }
                        break out;
                    }else if(mv2 != -1){
                        int x1 = mv2 % 10;
                        int y1 = mv2 / 10;
                        confirm[y1][x1] = 1;
                    }
                }else{
                    k++;
                }
                cycle1++;
            } //while in
        }//while out

        if(n == -1){
            System.out.println("移動できませんでした。");
            System.exit(0);
        }
    }

    public void syori(int z){
        //System.out.println("syori");
        int a = z; //zは攻撃座標
        int Y = a / 10; //攻撃座標のy座標を取得
        int X = a % 10; //攻撃座標のx座標を取得
        String sY = "error";
        int response;

        toEnemy.befkousin(X, Y);

        if(Y == 0){
            sY = "A";
        }else if(Y == 1){
            sY = "B";
        }else if(Y == 2){
            sY = "C";
        }else if(Y == 3){
            sY = "D";
        }else if(Y == 4){
            sY = "E";
        }
        System.out.println("「砲撃座標指定：" + sY + (X + 1) + "」");

        while(true){
            System.out.print("命中 0 (撃沈 3) or 波高し 1 or ハズレ 2: ");
            String stri = sc.next();
            if(stri.length() == 1){
                if(isNumber(stri)){
                    response = Integer.parseInt(stri);
                    HANT2 = HANT1;
                    HANT1 = response;
                    if(response == 0 || response == 1 || response == 2 || response == 3){
                        break;
                    }else{
                        System.out.println("0 か 1 か 2 か 3 ではありませんもう一度入力してください\n");
                    }
                }else{
                    System.out.println("入力が整数ではありません\n");
                }
            }else{
                System.out.println("文字列の長さが1ではありません\n");
            }
        }
        if(response == 0){ //自分の攻撃　命中
            toEnemy.teHit(X, Y);
        }else if(response == 1){ //自分の攻撃　波高
            if(HANT2 == 0 && action == 0){
                toEnemy.bluffenemy(X, Y);
            }else{
                toEnemy.teNamitaka(X, Y);
            }
        }else if(response == 2){ //自分の攻撃　外れ
            if(HANT2 == 0 && action == 0){
                toEnemy.bluffenemy(X, Y);
            }else{
                toEnemy.teHazure(X, Y);
            }
        }else{
            //命中＆撃沈処理
            sink(a);
            toEnemy.dieTE(X, Y);
            enemyPredict.dieEP(X, Y);
            kousin();
            killCount += 1;
            if(killCount == 4){ //勝ち判定
                System.out.println("You win!\n");
                System.exit(0);
            }
        }
    }

    public void damageMe(int c, int d){
        //System.out.println("damageMe");
        if(coordinate.damageMe(c, d) > 9){ //死亡処理
            kousin();
            toEnemy.dieTE(c, d);
            enemyPredict.dieEP(c, d);
            h = 1;
        }
    }

    public String hougaku(int n){
        if(n == 0){
            return "東";
        }else if(n == 1){
            return "西";
        }else if(n == 2){
            return "南";
        }else if(n == 3){
            return "北";
        }else{
            return "0";
        }
    }

    public boolean canmove(int x1, int y1){
        //System.out.println("canmove "+z1+" "+z2);
        if(coordinate.getCoordinate(x1, y1) == 1){ //自分の艦がいる
            for(int j = y-2; j < y+3; j++){
                if(j >= 0 && j <= 4){
                    if(j == y){
                        //何もしない
                    }else if(coordinate.getCoordinate(x, j) == 0){
                        return true;
                    }
                }
            }
            for(int i = x-2; i < x+3; i++){
                if(i >= 0 && i <= 4){
                    if(i == x){
                        //何もしない
                    }else if(coordinate.getCoordinate(i, y) == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void ugokisyuturyoku(int c1, int c2){
        int c = c2 - c1; //c1:動く前の座標　c2:動いた後の座標
        if(mode != 0){
            System.out.print("\n「Ship" + coordinate.whichShip(c2) + " が ");
        }else{
            System.out.print("\n「");
        }
        if(c==10){
            System.out.println("南 方向に 1 移動」");
        }else if(c==20){
            System.out.println("南 方向に 2 移動」");
        }else if(c==-10){
            System.out.println("北 方向に 1 移動」");
        }else if(c==-20){
            System.out.println("北 方向に 2 移動」");
        }else if(c==1){
            System.out.println("東 方向に 1 移動」");
        }else if(c==2){
            System.out.println("東 方向に 2 移動」");
        }else if(c==-1){
            System.out.println("西 方向に 1 移動」");
        }else if(c==-2){
            System.out.println("西 方向に 2 移動」");
        }

        int a1 = c1;
        int y1 = a1 / 10;
        int x1 = a1 % 10;
        int a2 = c2;
        int y2 = c2 / 10;
        int x2 = c2 % 10;
        if(mode != 0){
            System.out.println("("+x1+","+y1+")"+" --> "+"("+x2+","+y2+")");
        }
    }

    public void sink(int z){
        coordinate.sink(z);
        kousin();
    }

    public boolean canAttack(int x1, int y1){
        if(coordinate.getCoordinate(x1,y1) == 0){
            if(coordinate.existarround(x1,y1)){
                return true;
            }
        }
        return false;
    }
}
