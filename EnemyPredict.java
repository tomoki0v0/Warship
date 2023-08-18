//敵からの自分の見え方　評価値
public class EnemyPredict {

    private int[][] coordinateEP = new int[5][5]; //判定用座標

    private double[][] enemyPredict = new double[5][5];
    public final double rate0  = 0.2;  //敵が攻撃をしてきたときの影響割合(命中・　周囲)
    public final double rate00 = 0.1;  //敵が攻撃をしてきたときの影響割合(命中・その他)
    public final double rate2  = 0.5;  //敵が攻撃をしてきたときの影響割合(波高）
    public final double rate3  = 0.2;  //敵が攻撃をしてきたときの影響割合(ハズレ)
    public final double rate4  = 0.75; //自分が逃げた　ときの影響割合
    public final double rate5  = 0.05;  //自分が移動したときの影響割合
    public final double rate1  = 0.75;  //自分が攻撃したときの影響割合

    private double[] max = new double[4];

    private int xx, yy; //操作用座標
    private double sum;
    private int bl;

    EnemyPredict(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                enemyPredict[i][j] = 4;
            }
        }
    }

    public void shipjunjo(){
        //System.out.println("shipjunjo");
    	for(int n = 0; n < 4; n++){
    		max[n] = 0;
    	}
    	for(int n = 0; n < 4; n++){
    		for(int i = 0; i < 5; i++){
    			for(int j = 0; j < 5; j++){
    				if(max[n] < enemyPredict[j][i] && getCoordinateEP(i, j) == 1 && n == 0){
    					max[n] = enemyPredict[j][i]; //n=0のとき
    				}else if(max[n] < enemyPredict[j][i] && getCoordinateEP(i, j) == 1 && max[n-1] >= enemyPredict[j][i]){
    					max[n] = enemyPredict[j][i]; //n=1~3のとき
    				}
    			}
    		}
    	}
    }

    public double getshipjunjo(int m){
    	for(int n = 0; n < 4; n++){
    		if(m == n && max[n] != 0) return max[n];
    	}
    	return -1;
    }

    public double getEnemyPredict(int x, int y){
        return enemyPredict[y][x];
    }

    public void setCoordinateEP(int x, int y, int n){
        coordinateEP[y][x] = n;
    }

    public int getCoordinateEP(int x, int y){
        return coordinateEP[y][x];
    }

    public boolean existarroundEP(int x, int y){ //使うかわからん
        for(int j=y-1; j<y+2; j++){
            for(int i=x-1; i<x+2; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(getCoordinateEP(i, j) == 1){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void epHit(int x, int y){ //自分の攻撃　命中
    	//System.out.println("teHit X: "+x+", Y: "+y);
    	sum = 0.0;
    	for(int j = y-1; j < y+2; j++){
    		for(int i = x-1; i < x+2; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateEP[j][i] != -1){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(coordinateEP[j][i] != -1){
                            sum += enemyPredict[j][i] * rate0;
                            enemyPredict[j][i] *= (1 - rate0);
                        }
                    }
                }
            }
        }

    	for(int j = 0; j < 5; j++){
    		for(int i = 0; i < 5; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateEP[j][i] != -1){
                    if(x-2 < i && i < x+2 && y-2 < j && j < y+2){
                        //何もしない
                    }else{
                        if(coordinateEP[j][i] != -1){
                            sum += enemyPredict[j][i] * rate00;
                            enemyPredict[j][i] *= (1 - rate00);
                        }
                    }
                }
            }
    	}
    	enemyPredict[y][x] += sum;
    }

    public void epNamitaka(int x, int y){ //自分の攻撃　波高
    	int d = 0;
    	sum = 0;
    	for(int i = x-1; i < x+2; i++){
    		for(int j = y-1; j < y+2; j++){
    			if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
    				if(i == x && j == y){
                        //何もしない
                    }else if(coordinateEP[j][i] == -1){
    					d++;
    				}
    			}else{ //25マスより外
    				d++;
    			}
    		}
    	}
    	sum += enemyPredict[y][x] * (1 - rate2);
    	enemyPredict[y][x] *= rate2;
    	for(int i = x-1; i < x+2; i++){
    		for(int j = y-1; j < y+2; j++){
    			if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
    				if(i == x && j == y){
                        //何もしない
                    }else if(coordinateEP[j][i] != -1){
    					enemyPredict[j][i] += sum / (8 - d);
    				}
    			}
    		}
    	}
    }

    public void epHazure(int x, int y){ //自分の攻撃　外れ
        int d = 0;
        sum = 0.0;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(coordinateEP[j][i] == -1){
                    d++;
                }
            }
        }
        for(int j = y-1; j < y+2; j++){
            for(int i = x-1; i < x+2; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateEP[j][i] != -1){
                    sum += enemyPredict[j][i] * (1-rate4);
                    enemyPredict[j][i] = enemyPredict[j][i] * rate4;
                    d++;
                }
            }
        }

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4){
                    if(x-2 < i && i < x+2 && y-2 < j && j < y+2){
                        //何もしない
                    }else{
                        if(coordinateEP[j][i] != -1){
                            enemyPredict[j][i] += sum / (25-d);
                        }
                    }
                }
            }
        }
    }

    public void moveme(int c1, int c2){ //自分が移動したとき
        sum = 0.0;
        int c = c2 - c1;
        if(c==10){       //南 方向に 1 移動
            for(int j=0; j<1; j++){
                for(int i=0; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=1; j<5; j++){
                for(int i=0; i<5; i++){
                    enemyPredict[j][i] += sum / 20;
                }
            }
        }else if(c==20){ //南 方向に 2 移動
            for(int j=0; j<2; j++){
                for(int i=0; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=2; j<5; j++){
                for(int i=0; i<5; i++){
                    enemyPredict[j][i] += sum / 15;
                }
            }
        }else if(c==-10){//北 方向に 1 移動
            for(int j=4; j<5; j++){
                for(int i=0; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<4; j++){
                for(int i=0; i<5; i++){
                    enemyPredict[j][i] += sum / 20;
                }
            }
        }else if(c==-20){//北 方向に 2 移動
            for(int j=3; j<5; j++){
                for(int i=0; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<3; j++){
                for(int i=0; i<5; i++){
                    enemyPredict[j][i] += sum / 15;
                }
            }
        }else if(c==1){  //東 方向に 1 移動
            for(int j=0; j<5; j++){
                for(int i=0; i<1; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<5; j++){
                for(int i=1; i<5; i++){
                    enemyPredict[j][i] += sum / 20;
                }
            }
        }else if(c==2){  //東 方向に 2 移動
            for(int j=0; j<5; j++){
                for(int i=0; i<2; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<5; j++){
                for(int i=2; i<5; i++){
                    enemyPredict[j][i] += sum / 15;
                }
            }
        }else if(c==-1){ //西 方向に 1 移動
            for(int j=0; j<5; j++){
                for(int i=4; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<5; j++){
                for(int i=0; i<4; i++){
                    enemyPredict[j][i] += sum / 20;
                }
            }
        }else if(c==-2){ //西 方向に 2 移動
            for(int j=0; j<5; j++){
                for(int i=3; i<5; i++){
                    sum += enemyPredict[j][i] * rate5;
                    enemyPredict[j][i] = enemyPredict[j][i] * (1 - rate5);
                }
            }

            for(int j=0; j<5; j++){
                for(int i=0; i<3; i++){
                    enemyPredict[j][i] += sum / 15;
                }
            }
        }
    }

    public void runme(int z1, int z2){ //自分が逃げたとき
        //移動前座標
        int a1 = z1;
        int y1 = a1 / 10;
        int x1 = a1 % 10;
        //移動後座標
        int a2 = z2;
        int y2 = a2 / 10;
        int x2 = a2 % 10;

        enemyPredict[y2][x2] += enemyPredict[y1][x1] * rate4;
        enemyPredict[y1][x1] *= (1-rate4);
    }

    public void attackme(int x, int y){ //自分の攻撃
        sum = 0.0;
        if(y == 0){
            if(x == 0){
                sum += enemyPredict[0][0] * (1 - rate1);
                enemyPredict[0][0] = enemyPredict[0][0] * rate1;

                enemyPredict[0][1] += sum / 3.0;
                enemyPredict[1][0] += sum / 3.0;
                enemyPredict[1][1] += sum / 3.0;
            }else if(x == 1){
                sum += enemyPredict[0][1] * (1-rate1);
                enemyPredict[0][1] = enemyPredict[0][1] * rate1;

                enemyPredict[0][0] += sum / 5.0;
                enemyPredict[0][2] += sum / 5.0;
                enemyPredict[1][0] += sum / 5.0;
                enemyPredict[1][1] += sum / 5.0;
                enemyPredict[1][2] += sum / 5.0;

            }else if(x == 2){
                sum += enemyPredict[0][2] * (1-rate1);
                enemyPredict[0][2] = enemyPredict[0][2] * rate1;

                enemyPredict[0][1] += sum / 5.0;
                enemyPredict[0][3] += sum / 5.0;
                enemyPredict[1][1] += sum / 5.0;
                enemyPredict[1][2] += sum / 5.0;
                enemyPredict[1][3] += sum / 5.0;
            }else if(x == 3){
                sum += enemyPredict[0][3] * (1-rate1);
                enemyPredict[0][3] = enemyPredict[0][3] * rate1;

                enemyPredict[0][2] += sum / 5.0;
                enemyPredict[0][4] += sum / 5.0;
                enemyPredict[1][2] += sum / 5.0;
                enemyPredict[1][3] += sum / 5.0;
                enemyPredict[1][4] += sum / 5.0;
            }else if(x == 4){
                sum += enemyPredict[0][4] * (1-rate1);
                enemyPredict[0][4] = enemyPredict[0][4] * rate1;

                enemyPredict[0][3] += sum / 3.0;
                enemyPredict[1][3] += sum / 3.0;
                enemyPredict[1][4] += sum / 3.0;
            }

        }else if(y == 1){
            if(x == 0){
                sum += enemyPredict[1][0] * (1 - rate1);
                enemyPredict[1][0] = enemyPredict[1][0] * rate1;

                enemyPredict[0][0] += sum / 5.0;
                enemyPredict[0][1] += sum / 5.0;
                enemyPredict[1][1] += sum / 5.0;
                enemyPredict[2][0] += sum / 5.0;
                enemyPredict[2][1] += sum / 5.0;
            }else if(x == 1){
                sum += enemyPredict[1][1] * (1-rate1);
                enemyPredict[1][1] = enemyPredict[1][1] * rate1;

                enemyPredict[0][0] += sum / 8.0;
                enemyPredict[0][1] += sum / 8.0;
                enemyPredict[0][2] += sum / 8.0;
                enemyPredict[1][0] += sum / 8.0;
                enemyPredict[1][2] += sum / 8.0;
                enemyPredict[2][0] += sum / 8.0;
                enemyPredict[2][1] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
            }else if(x == 2){
                sum += enemyPredict[1][2] * (1-rate1);
                enemyPredict[1][2] = enemyPredict[1][2] * rate1;

                enemyPredict[0][1] += sum / 8.0;
                enemyPredict[0][2] += sum / 8.0;
                enemyPredict[0][3] += sum / 8.0;
                enemyPredict[1][1] += sum / 8.0;
                enemyPredict[1][3] += sum / 8.0;
                enemyPredict[2][1] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[2][3] += sum / 8.0;
            }else if(x == 3){
                sum += enemyPredict[1][3] * (1-rate1);
                enemyPredict[1][3] = enemyPredict[1][3] * rate1;

                enemyPredict[0][2] += sum / 8.0;
                enemyPredict[0][3] += sum / 8.0;
                enemyPredict[0][4] += sum / 8.0;
                enemyPredict[1][2] += sum / 8.0;
                enemyPredict[1][4] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[2][3] += sum / 8.0;
                enemyPredict[2][4] += sum / 8.0;
            }else if(x == 4){
                sum += enemyPredict[1][4] * (1-rate1);
                enemyPredict[1][4] = enemyPredict[1][4] * rate1;

                enemyPredict[0][3] += sum / 5.0;
                enemyPredict[0][4] += sum / 5.0;
                enemyPredict[1][3] += sum / 5.0;
                enemyPredict[2][3] += sum / 5.0;
                enemyPredict[2][4] += sum / 5.0;
            }

        }else if(y == 2){
            if(x == 0){
                sum += enemyPredict[2][0] * (1 - rate1);
                enemyPredict[2][0] = enemyPredict[2][0] * rate1;

                enemyPredict[1][0] += sum / 5.0;
                enemyPredict[1][1] += sum / 5.0;
                enemyPredict[2][1] += sum / 5.0;
                enemyPredict[3][0] += sum / 5.0;
                enemyPredict[3][1] += sum / 5.0;
            }else if(x == 1){
                sum += enemyPredict[2][1] * (1-rate1);
                enemyPredict[2][1] = enemyPredict[2][1] * rate1;

                enemyPredict[1][0] += sum / 8.0;
                enemyPredict[1][1] += sum / 8.0;
                enemyPredict[1][2] += sum / 8.0;
                enemyPredict[2][0] += sum / 8.0;
                enemyPredict[2][3] += sum / 8.0;
                enemyPredict[3][0] += sum / 8.0;
                enemyPredict[3][1] += sum / 8.0;
                enemyPredict[3][2] += sum / 8.0;
            }else if(x == 2){
                sum += enemyPredict[2][2] * (1-rate1);
                enemyPredict[2][2] = enemyPredict[2][2] * rate1;

                enemyPredict[1][1] += sum / 8.0;
                enemyPredict[1][2] += sum / 8.0;
                enemyPredict[1][3] += sum / 8.0;
                enemyPredict[2][1] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[3][1] += sum / 8.0;
                enemyPredict[3][2] += sum / 8.0;
                enemyPredict[3][3] += sum / 8.0;
            }else if(x == 3){
                sum += enemyPredict[2][3] * (1-rate1);
                enemyPredict[2][3] = enemyPredict[2][3] * rate1;

                enemyPredict[1][2] += sum / 8.0;
                enemyPredict[1][3] += sum / 8.0;
                enemyPredict[1][4] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[2][4] += sum / 8.0;
                enemyPredict[3][2] += sum / 8.0;
                enemyPredict[3][3] += sum / 8.0;
                enemyPredict[3][4] += sum / 8.0;
            }else if(x == 4){
                sum += enemyPredict[2][4] * (1-rate1);
                enemyPredict[2][4] = enemyPredict[2][4] * rate1;

                enemyPredict[1][3] += sum / 5.0;
                enemyPredict[1][4] += sum / 5.0;
                enemyPredict[2][3] += sum / 5.0;
                enemyPredict[3][3] += sum / 5.0;
                enemyPredict[3][4] += sum / 5.0;
            }

        }else if(y == 3){
            if(x == 0){
                sum += enemyPredict[3][0] * (1 - rate1);
                enemyPredict[3][0] = enemyPredict[3][0] * rate1;

                enemyPredict[2][0] += sum / 5.0;
                enemyPredict[2][1] += sum / 5.0;
                enemyPredict[3][1] += sum / 5.0;
                enemyPredict[4][0] += sum / 5.0;
                enemyPredict[4][1] += sum / 5.0;
            }else if(x == 1){
                sum += enemyPredict[3][1] * (1-rate1);
                enemyPredict[3][1] = enemyPredict[3][1] * rate1;

                enemyPredict[2][0] += sum / 8.0;
                enemyPredict[2][1] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[3][0] += sum / 8.0;
                enemyPredict[3][2] += sum / 8.0;
                enemyPredict[4][0] += sum / 8.0;
                enemyPredict[4][1] += sum / 8.0;
                enemyPredict[4][2] += sum / 8.0;
            }else if(x == 2){
                sum += enemyPredict[3][2] * (1-rate1);
                enemyPredict[3][2] = enemyPredict[3][2] * rate1;

                enemyPredict[2][1] += sum / 8.0;
                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[2][3] += sum / 8.0;
                enemyPredict[3][1] += sum / 8.0;
                enemyPredict[3][3] += sum / 8.0;
                enemyPredict[4][1] += sum / 8.0;
                enemyPredict[4][2] += sum / 8.0;
                enemyPredict[4][3] += sum / 8.0;
            }else if(x == 3){
                sum += enemyPredict[3][3] * (1-rate1);
                enemyPredict[3][3] = enemyPredict[3][3] * rate1;

                enemyPredict[2][2] += sum / 8.0;
                enemyPredict[2][3] += sum / 8.0;
                enemyPredict[2][4] += sum / 8.0;
                enemyPredict[3][2] += sum / 8.0;
                enemyPredict[3][4] += sum / 8.0;
                enemyPredict[4][2] += sum / 8.0;
                enemyPredict[4][3] += sum / 8.0;
                enemyPredict[4][4] += sum / 8.0;
            }else if(x == 4){
                sum += enemyPredict[3][4] * (1-rate1);
                enemyPredict[3][4] = enemyPredict[3][4] * rate1;

                enemyPredict[2][3] += sum / 5.0;
                enemyPredict[2][4] += sum / 5.0;
                enemyPredict[3][3] += sum / 5.0;
                enemyPredict[4][3] += sum / 5.0;
                enemyPredict[4][4] += sum / 5.0;
            }

        }else if(y == 4){
            if(x == 0){
                sum += enemyPredict[4][0] * (1 - rate1);
                enemyPredict[4][0] = enemyPredict[4][0] * rate1;

                enemyPredict[3][0] += sum / 3.0;
                enemyPredict[3][1] += sum / 3.0;
                enemyPredict[4][1] += sum / 3.0;
            }else if(x == 1){
                sum += enemyPredict[4][1] * (1-rate1);
                enemyPredict[4][1] = enemyPredict[4][1] * rate1;

                enemyPredict[3][0] += sum / 5.0;
                enemyPredict[3][1] += sum / 5.0;
                enemyPredict[3][2] += sum / 5.0;
                enemyPredict[4][0] += sum / 5.0;
                enemyPredict[4][2] += sum / 5.0;
            }else if(x == 2){
                sum += enemyPredict[4][2] * (1-rate1);
                enemyPredict[4][2] = enemyPredict[4][2] * rate1;

                enemyPredict[3][1] += sum / 5.0;
                enemyPredict[3][2] += sum / 5.0;
                enemyPredict[3][3] += sum / 5.0;
                enemyPredict[4][1] += sum / 5.0;
                enemyPredict[4][3] += sum / 5.0;
            }else if(x == 3){
                sum += enemyPredict[4][3] * (1-rate1);
                enemyPredict[4][3] = enemyPredict[4][3] * rate1;

                enemyPredict[3][2] += sum / 5.0;
                enemyPredict[3][3] += sum / 5.0;
                enemyPredict[3][4] += sum / 5.0;
                enemyPredict[4][2] += sum / 5.0;
                enemyPredict[4][4] += sum / 5.0;
            }else if(x == 4){
                sum += enemyPredict[4][4] * (1-rate1);
                enemyPredict[4][4] = enemyPredict[4][4] * rate1;

                enemyPredict[3][3] += sum / 3.0;
                enemyPredict[3][4] += sum / 3.0;
                enemyPredict[4][3] += sum / 3.0;
            }
        }
    }

    public int idounosyori(int z){ //十字８マス
        //System.out.println("idounosyori2");
        if(z == -1){
            return -1;
        }
        int a = z;
        int y = a / 10;
        int x = a % 10;

        int bx = bl % 10;
        int by = bl / 10;

        double min = 100;
        int zahyo = -1;
        double p = 0.5;
        int n = 1;
        int bbx,bby,bl2;
        for(int j = y-2; j < y+3; j++){
            if(j >= 0 && j <= 4){
                bby = j - y + by;
                bl2 = bby * 10 + bx;
                if(coordinateEP[j][x] == 0 && canmoveEP2(bl, bl2)){
                    if(j == y){
                        //何もしない
                    }else{
                        if(min > enemyPredict[j][x]){
                            min = enemyPredict[j][x];
                            zahyo = j * 10 + x;
                        }else if(min == enemyPredict[j][x] && Math.random() < p){
                            min = enemyPredict[j][x];
                            zahyo = j * 10 + x;
                            n++;
                            p *= (n / (n+1));
                        }
                    }
                }
            }
        }
        for(int i = x-2; i < x+3; i++){
            if(i >= 0 && i <= 4){
                bbx = i - x + bx;
                bl2 = by * 10 + bbx;
                if(coordinateEP[y][i] == 0 && canmoveEP2(bl, bl2)){
                    if(i == x){
                        //何もしない
                    }else{
                        if(min > enemyPredict[y][i]){
                            min = enemyPredict[y][i];
                            zahyo = y * 10 + i;
                        }else if(min == enemyPredict[y][i] && Math.random() < p){
                            min = enemyPredict[y][i];
                            zahyo = y * 10 + i;
                            n++;
                            p *= (n / (n+1));
                        }
                    }
                }
            }
        }
        bl = -1;
        return zahyo;
    }

    public int idounosyori2(int z){ //十字８マス
        //System.out.println("idounosyori2");
        if(z == -1){
            return -1;
        }
        int a = z;
        int y = a / 10;
        int x = a % 10;

        double min = 100;
        int zahyo = -1;
        double p = 0.5;
        int n = 1;
        for(int j = y-2; j < y+3; j++){
            if(j >= 0 && j <= 4){
                if(coordinateEP[j][x] == 0){
                    if(j == y){
                        //何もしない
                    }else{
                        if(min > enemyPredict[j][x]){
                            min = enemyPredict[j][x];
                            zahyo = j * 10 + x;
                        }else if(min == enemyPredict[j][x] && Math.random() < p){
                            min = enemyPredict[j][x];
                            zahyo = j * 10 + x;
                            n++;
                            p *= (n / (n+1));
                        }
                    }
                }
            }
        }
        for(int i = x-2; i < x+3; i++){
            if(i >= 0 && i <= 4){
                if(coordinateEP[y][i] == 0){
                    if(i == x){
                        //何もしない
                    }else{
                        if(min > enemyPredict[y][i]){
                            min = enemyPredict[y][i];
                            zahyo = y * 10 + i;
                        }else if(min == enemyPredict[y][i] && Math.random() < p){
                            min = enemyPredict[y][i];
                            zahyo = y * 10 + i;
                            n++;
                            p *= (n / (n+1));
                        }
                    }
                }
            }
        }
        bl = -1;
        return zahyo;
    }

    public int bluff1(int mv1){ //ブラフの処理1
        int zahyo = -1;
        int sxy1 = -1;
        int sxy2 = -1;
        int sxy3 = -1;
        int sxy4 = -1;
        double s1 = 0, s2 = 0, s3 = 0, s4 = 0;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(coordinateEP[j][i] == 1){ //とりあえず船のいるところの評価値を得る
                    if(s1 == 0 && sxy1 == -1){
                        s1  = enemyPredict[j][i];
                        sxy1 = j * 10 + i;
                    }else if(s2 == 0 && sxy2 == -1){
                        s2  = enemyPredict[j][i];
                        sxy2 = j * 10 + i;
                    }else if(s3 == 0 && sxy3 == -1){
                        s3  = enemyPredict[j][i];
                        sxy3 = j * 10 + i;
                    }else if(s4 == 0 && sxy4 == -1){
                        s4  = enemyPredict[j][i];
                        sxy4 = j * 10 + i;
                    }
                }
            }
        }
        double n = Math.random() * 30;
        //以下、評価値が最も高いところ(攻撃されている座標)以外からランダムで選ぶ
        if(mv1 == sxy1){ //攻撃された艦が sxy1 にいる場合
            bl = sxy1;
            if(n>2 && s2 != 0 && sxy2 != -1){
                zahyo = sxy2;
            }else if(n>1 && s3 != 0 && sxy3 != -1){
                zahyo = sxy3;
            }else if(s4 != 0 && sxy4 != -1){
                zahyo = sxy4;
            }
        }else if(mv1 == sxy2){ //攻撃された艦が sxy2 にいる場合
            bl = sxy2;
            if(n>2 && s1 != 0 && sxy1 != -1){
                zahyo = sxy1;
            }else if(n>1 && s3 != 0 && sxy3 != -1){
                zahyo = sxy3;
            }else if(s4 != 0 && sxy4 != -1){
                zahyo = sxy4;
            }
        }else if(mv1 == sxy3){ //攻撃された艦が sxy3 にいる場合
            bl = sxy3;
            if(n>2 && s1 != 0 && sxy1 != -1){
                zahyo = sxy1;
            }else if(n>1 && s2 != 0 && sxy2 != -1){
                zahyo = sxy2;
            }else if(s4 != 0 && sxy4 != -1){
                zahyo = sxy4;
            }
        }else if(mv1 == sxy4){ //攻撃された艦が sxy4 にいる場合
            bl = sxy4;
            if(n>2 && s1 != 0 && sxy1 != -1){
                zahyo = sxy1;
            }else if(n>1 && s2 != 0 && sxy2 != -1){
                zahyo = sxy2;
            }else if(s3 != 0 && sxy3 != -1){
                zahyo = sxy3;
            }
        }
        return zahyo;
    }

    public void dieEP(int x, int y){
        double sum = 0;
        int d = 0;
        sum = enemyPredict[y][x];
        enemyPredict[y][x] = 0;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(coordinateEP[j][i] == -1){
                    d++;
                }
            }
        }

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(i == x && j == y){
                    //何もしない
                }else{
                    if(coordinateEP[j][i] != -1){
                        enemyPredict[j][i] += sum / (25 - d);
                    }
                }
            }
        }
    }

    public boolean canmoveEP(int x, int y){
        //System.out.println("canmoveEP x: "+x+", y: "+y);
        for(int j = y-2; j < y+3; j++){
            for(int i = x; i < x+1; i++){
                if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
                    if(j == y && i == x){
                        //何もしない
                    }else if(getCoordinateEP(i, j) == 0){
                        return true;
                    }
                }
            }
        }
        for(int j = y; j < y+1; j++){
            for(int i = x-2; i < x+3; i++){
                if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
                    if(j == y && i == x){
                        //何もしない
                    }else if(getCoordinateEP(i, j) == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canmoveEP2(int z1, int z2){
        int a1 = z1;
        int x1 = a1 % 10;
        int y1 = a1 / 10;

        int a2 = z2;
        int x2 = a2 % 10;
        int y2 = a2 / 10;
        if(x1 < 0 || 4 < x1 || y1 < 0 || 4 < y1 || x2 < 0 || 4 < x2 || y2 < 0 || 4 < y2) return false;
        if(coordinateEP[y1][x1] == 1 && coordinateEP[y2][x2] == 0) return true;
        return false;
    }

    public void kakuninEP(){ //デバッグ用
        System.out.print("\n\n<kakuninEP>    Max:  ");
        int n = 0;
        double max = -1;
        double sum = 0;
        for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                sum += enemyPredict[j][i];
                if(max < enemyPredict[j][i]){
                    max = enemyPredict[j][i];
                }
            }
        }
        System.out.print(String.format("%.3f", max)+"  ");
        Max: for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                if(max == enemyPredict[j][i]){
                    System.out.print("("+j+", "+i+") ");
                    n++;
                    if(n==3) {
                        System.out.println("…");
                        break Max;
                    }else System.out.print(", ");
                }
            }
        }
        if(n<3)System.out.println();

        for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                System.out.print("("+j+", "+i+") ");
                if(enemyPredict[j][i] < 10) System.out.print(" ");
                System.out.print(String.format("%.3f", enemyPredict[j][i])+"  ");
            }
            System.out.println();
        }
        System.out.println("enemyPredict sum: "+String.format("%.3f", sum)+"  ");
    }
}
