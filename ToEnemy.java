//敵のいる場所の予測　評価値
public class ToEnemy {

    private int[][] coordinateTE = new int[5][5]; //使えるマスかどうか判断するときに使う

    private double[][] toEnemy = new double[5][5];
    public final double rate1 = 0.75;  //敵が攻撃をしてきたときの影響割合
    //public final double rate3 = 0.9;  //not exist
    public final double rate0  = 0.2; //自分が攻撃して、命中だったときの影響割合（周囲８マス）
    public final double rate00 = 0.1; //自分が攻撃して、命中だったときの影響割合（その他）
    public final double rate2 = 0.2; //自分が攻撃して、波高だったときの影響割合
    public final double rate4 = 0.1;  //自分が攻撃して、外れだったときの影響割合
    public final double rate5 = 0.05; //敵が移動をしてきたときの影響割合
    public final double rate6 = 0.75; //前ターン：自軍が攻撃　次ターン：敵軍移動

    private double[] max = new double[8];

    private double sum;
    private int masCount = 0; //使えなくなったマスの数
    private int OL = 15; //重なろうとするときの最小値

    private int befx, befy; //前回攻撃した座標の保持
    private int befd, befn; //前回敵が移動した方角、マス数の保持

    ToEnemy(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                toEnemy[j][i] = 4;
            }
        }
    }

    public void junjokousinTE(){
        //System.out.println("junjokousinTE");
    	for(int n = 0; n < 8; n++){
    		max[n] = 0;
    	}
    	for(int n = 0; n < 8; n++){
    		for(int i = 0; i < 5; i++){
    			for(int j = 0; j < 5; j++){
    				if(max[n] < toEnemy[j][i] && n == 0){
    					max[n] = toEnemy[j][i]; //n=0のとき
    				}else if(max[n] < toEnemy[j][i] && max[n-1] > toEnemy[j][i]){
    					max[n] = toEnemy[j][i]; //n=1~7のとき
    				}
    			}
    		}
    	}
    }

    public double getmax(int m){
    	for(int n = 0; n < 8; n++){
    		if(m == n && max[n] != 0) return max[n];
    	}
    	return -1;
    }

    public int searchTE(double N){
        int zahyo = -1;
        double p = 0.5;
        int n = 1;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(toEnemy[j][i] == N){
                    if(canattackTE(i, j)){
                        if(zahyo == -1){
                            zahyo = j*10+i;
                        }else if(Math.random() < p){
                            zahyo = j*10+i;
                            n++;
                            p *= (n / (n+1));
                        }
                    }
                }
            }
        }
        return zahyo;
    }

    public double getToEnemy(int x, int y){
        return toEnemy[y][x];
    }

    public void setCoordinateTE(int x, int y, int n){
        coordinateTE[y][x] = n;
    }

    public int getCoordinateTE(int x, int y){
        return coordinateTE[y][x];
    }

    public boolean existarroundTE(int x, int y){
        for(int j=y-1; j<y+2; j++){
            for(int i=x-1; i<x+2; i++){
                if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(getCoordinateTE(i, j) == 1){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void teHit(int x, int y){
    	//System.out.println("teHit X: "+x+", Y: "+y);
    	sum = 0.0;
    	for(int j = y-1; j < y+2; j++){
            for(int i = x-1; i < x+2; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateTE[j][i] != -1){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(getCoordinateTE(i, j) != -1){
                            sum += toEnemy[j][i] * rate0;
                            toEnemy[j][i] *= (1 - rate0);
                        }
                    }
                }
            }
    	}

        for(int j = 0; j < 5; j++){
            for(int i = 0; i < 5; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateTE[j][i] != -1){
                    if(x-2 < i && i < x+2 && y-2 < j && j < y+2){
                        //何もしない
                    }else{
                        if(getCoordinateTE(i, j) != -1){
                            sum += toEnemy[j][i] * rate00;
                            toEnemy[j][i] *= (1 - rate00);
                        }
                    }
                }
            }
        }
    	toEnemy[y][x] += sum;
    }

    public void teNamitaka(int x, int y){
    	int d = 0;
    	sum = 0;
    	for(int i = x-1; i < x+2; i++){
    		for(int j = y-1; j < y+2; j++){
    			if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
    				if(i == x && j == y){
                        //何もしない
                    }else if(getCoordinateTE(i, j) == -1){
    					d++;
    				}
    			}else{ //25マスより外
    				d++;
    			}
    		}
    	}
    	sum += toEnemy[y][x] * (1 - rate2);
    	toEnemy[y][x] *= rate2;
    	for(int i = x-1; i < x+2; i++){
    		for(int j = y-1; j < y+2; j++){
    			if(i >= 0 && i <= 4 && j>= 0 && j <= 4){
    				if(i == x && j == y){
                        //何もしない
                    }else if(getCoordinateTE(i, j) != -1){
    					toEnemy[j][i] += sum / (8 - d);
    				}
    			}
    		}
    	}
    }

    public void teHazure(int x, int y){
    	int d = 0;
    	sum = 0.0;

    	for(int i = 0; i < 5; i++){
    		for(int j = 0; j < 5; j++){
    			if(coordinateTE[j][i] == -1){
    				d++;
    			}
    		}
    	}
    	for(int j = y-1; j < y+2; j++){
    		for(int i = x-1; i < x+2; i++){
    			if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateTE[j][i] != -1){
    				sum += toEnemy[j][i] * (1-rate4);
    				toEnemy[j][i] = toEnemy[j][i] * rate4;
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
    					if(coordinateTE[j][i] != -1){
    						toEnemy[j][i] += sum / (25-d);
    					}
    				}
    			}
    		}
    	}
    }

    public void moveenemy(int x, int y){
        //x0 東, x1 西, x2 南, x3 北
        sum = 0.0;
        if(x == 0){
            if(y == 1){ //東に1マス
                for(int j=0; j<5; j++){
                    for(int i=0; i<1; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<5; j++){
                    for(int i=1; i<5; i++){
                        toEnemy[j][i] += sum / 20;
                    }
                }
            }
            if(y == 2){ //東に2マス
                for(int j=0; j<5; j++){
                    for(int i=0; i<2; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<5; j++){
                    for(int i=2; i<5; i++){
                        toEnemy[j][i] += sum / 15;
                    }
                }
            }
        }
        if(x == 1){
            if(y == 1){ //西に1マス
                for(int j=0; j<5; j++){
                    for(int i=4; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<5; j++){
                    for(int i=0; i<4; i++){
                        toEnemy[j][i] += sum / 20;
                    }
                }
            }
            if(y == 2){ //西に2マス
                for(int j=0; j<5; j++){
                    for(int i=3; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<5; j++){
                    for(int i=0; i<3; i++){
                        toEnemy[j][i] += sum / 15;
                    }
                }
            }
        }
        if(x == 2){
            if(y == 1){ //南に1マス
                for(int j=0; j<1; j++){
                    for(int i=0; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=1; j<5; j++){
                    for(int i=0; i<5; i++){
                        toEnemy[j][i] += sum / 20;
                    }
                }
            }
            if(y == 2){ //南に2マス
                for(int j=0; j<2; j++){
                    for(int i=0; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=2; j<5; j++){
                    for(int i=0; i<5; i++){
                        toEnemy[j][i] += sum / 15;
                    }
                }
            }
        }
        if(x == 3){
            if(y == 1){ //北に1マス
                for(int j=4; j<5; j++){
                    for(int i=0; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<4; j++){
                    for(int i=0; i<5; i++){
                        toEnemy[j][i] += sum / 20;
                    }
                }
            }
            if(y == 2){ //北に2マス
                for(int j=3; j<5; j++){
                    for(int i=0; i<5; i++){
                        sum += toEnemy[j][i] * rate5;
                        toEnemy[j][i] = toEnemy[j][i] * (1 - rate5);
                    }
                }

                for(int j=0; j<3; j++){
                    for(int i=0; i<5; i++){
                        toEnemy[j][i] += sum / 15;
                    }
                }
            }
        }
    }

    public boolean runenemy(int d, int n){
        befn = n;
        befd = d;
        System.out.println("runenemy befx: "+befx+", befy: "+befy);
        sum = 0;
        sum += toEnemy[befy][befx] * rate6;
        toEnemy[befy][befx] = toEnemy[befy][befx] * (1 - rate6);
        if(d==0){ //東
            if(n==1){
                if(0 <= befx+1 && befx+1 <= 4 && 0 <= befy && befy <= 4){
                    toEnemy[befy][befx+1] += sum;
                    return true;
                }else{
                    return false;
                }
            }
            if(n==2){
                if(0 <= befx+2 && befx+2 <= 4 && 0 <= befy && befy <= 4){
                    toEnemy[befy][befx+2] += sum;
                    return true;
                }else{
                    return false;
                }
            }
        }else if(d==1){ //西
            if(n==1){
                if(0 <= befx-1 && befx-1 <= 4 && 0 <= befy && befy <= 4){
                    toEnemy[befy][befx-1] += sum;
                    return true;
                }else{
                    return false;
                }
            }
            if(n==2){
                if(0 <= befx-2 && befx-2 <= 4 && 0 <= befy && befy <= 4){
                    toEnemy[befy][befx-2] += sum;
                    return true;
                }else{
                    return false;
                }
            }
        }else if(d==2){ //南
            if(n==1){
                if(0 <= befx && befx <= 4 && 0 <= befy+1 && befy+1 <= 4){
                    toEnemy[befy+1][befx] += sum;
                    return true;
                }else{
                    return false;
                }
            }
            if(n==2){
                if(0 <= befx && befx <= 4 && 0 <= befy+2 && befy+2 <= 4){
                    toEnemy[befy+2][befx] += sum;
                    return true;
                }else{
                    return false;
                }
            }
        }else if(d==3){ //北
            if(n==1){
                if(0 <= befx && befx <= 4 && 0 <= befy-1 && befy-1 <= 4){
                    toEnemy[befy-1][befx] += sum;
                    return true;
                }else{
                    return false;
                }
            }
            if(n==2){
                if(0 <= befx && befx <= 4 && 0 <= befy-2 && befy-2 <= 4){
                    toEnemy[befy-2][befx] += sum;
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public void notrunenemy(){
        int x = befx;
        int y = befy;
    	sum = 0.0;
    	for(int j = y-1; j < y+2; j++){
            for(int i = x-1; i < x+2; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateTE[j][i] != -1){
                    if(j == y && i == x){
                        //何もしない
                    }else{
                        if(getCoordinateTE(i, j) != -1){
                            sum += toEnemy[j][i] * rate0;
                            toEnemy[j][i] *= (1 - rate0);
                        }
                    }
                }
            }
    	}

        for(int j = 0; j < 5; j++){
            for(int i = 0; i < 5; i++){
                if(i >= 0 && i <= 4 && j >= 0 && j <= 4 && coordinateTE[j][i] != -1){
                    if(x-2 < i && i < x+2 && y-2 < j && j < y+2){
                        //何もしない
                    }else{
                        if(getCoordinateTE(i, j) != -1){
                            sum += toEnemy[j][i] * rate00;
                            toEnemy[j][i] *= (1 - rate00);
                        }
                    }
                }
            }
        }
    	toEnemy[y][x] += sum;
    }

    public void bluffenemy(int x, int y){
        System.out.println("bluffenemy");
        sum = 0;
        sum += toEnemy[y][x] * rate6;
        toEnemy[y][x] = toEnemy[y][x] * (1 - rate6);
        if(befd==0){ //東
            if(befn==1) toEnemy[y][x-1] += sum;
            if(befn==2) toEnemy[y][x-2] += sum;
        }else if(befd==1){ //西
            if(befn==1) toEnemy[y][x+1] += sum;
            if(befn==2) toEnemy[y][x+2] += sum;
        }else if(befd==2){ //南
            if(befn==1) toEnemy[y-1][x] += sum;
            if(befn==2) toEnemy[y-2][x] += sum;
        }else if(befd==3){ //北
            if(befn==1) toEnemy[y+1][x] += sum;
            if(befn==2) toEnemy[y+2][x] += sum;
        }
    }

    public void attackenemyTE(int x, int y){
        sum = 0.0;
        if(y == 0){
            if(x == 0){
                sum += toEnemy[0][0] * (1 - rate1);
                toEnemy[0][0] = toEnemy[0][0] * rate1;

                toEnemy[0][1] += sum / 3.0;
                toEnemy[1][0] += sum / 3.0;
                toEnemy[1][1] += sum / 3.0;
            }else if(x == 1){
                sum += toEnemy[0][1] * (1-rate1);
                toEnemy[0][1] = toEnemy[0][1] * rate1;

                toEnemy[0][0] += sum / 5.0;
                toEnemy[0][2] += sum / 5.0;
                toEnemy[1][0] += sum / 5.0;
                toEnemy[1][1] += sum / 5.0;
                toEnemy[1][2] += sum / 5.0;

            }else if(x == 2){
                sum += toEnemy[0][2] * (1-rate1);
                toEnemy[0][2] = toEnemy[0][2] * rate1;

                toEnemy[0][1] += sum / 5.0;
                toEnemy[0][3] += sum / 5.0;
                toEnemy[1][1] += sum / 5.0;
                toEnemy[1][2] += sum / 5.0;
                toEnemy[1][3] += sum / 5.0;
            }else if(x == 3){
                sum += toEnemy[0][3] * (1-rate1);
                toEnemy[0][3] = toEnemy[0][3] * rate1;

                toEnemy[0][2] += sum / 5.0;
                toEnemy[0][4] += sum / 5.0;
                toEnemy[1][2] += sum / 5.0;
                toEnemy[1][3] += sum / 5.0;
                toEnemy[1][4] += sum / 5.0;
            }else if(x == 4){
                sum += toEnemy[0][4] * (1-rate1);
                toEnemy[0][4] = toEnemy[0][4] * rate1;

                toEnemy[0][3] += sum / 3.0;
                toEnemy[1][3] += sum / 3.0;
                toEnemy[1][4] += sum / 3.0;
            }

        }else if(y == 1){
            if(x == 0){
                sum += toEnemy[1][0] * (1 - rate1);
                toEnemy[1][0] = toEnemy[1][0] * rate1;

                toEnemy[0][0] += sum / 5.0;
                toEnemy[0][1] += sum / 5.0;
                toEnemy[1][1] += sum / 5.0;
                toEnemy[2][0] += sum / 5.0;
                toEnemy[2][1] += sum / 5.0;
            }else if(x == 1){
                sum += toEnemy[1][1] * (1-rate1);
                toEnemy[1][1] = toEnemy[1][1] * rate1;

                toEnemy[0][0] += sum / 8.0;
                toEnemy[0][1] += sum / 8.0;
                toEnemy[0][2] += sum / 8.0;
                toEnemy[1][0] += sum / 8.0;
                toEnemy[1][2] += sum / 8.0;
                toEnemy[2][0] += sum / 8.0;
                toEnemy[2][1] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
            }else if(x == 2){
                sum += toEnemy[1][2] * (1-rate1);
                toEnemy[1][2] = toEnemy[1][2] * rate1;

                toEnemy[0][1] += sum / 8.0;
                toEnemy[0][2] += sum / 8.0;
                toEnemy[0][3] += sum / 8.0;
                toEnemy[1][1] += sum / 8.0;
                toEnemy[1][3] += sum / 8.0;
                toEnemy[2][1] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[2][3] += sum / 8.0;
            }else if(x == 3){
                sum += toEnemy[1][3] * (1-rate1);
                toEnemy[1][3] = toEnemy[1][3] * rate1;

                toEnemy[0][2] += sum / 8.0;
                toEnemy[0][3] += sum / 8.0;
                toEnemy[0][4] += sum / 8.0;
                toEnemy[1][2] += sum / 8.0;
                toEnemy[1][4] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[2][3] += sum / 8.0;
                toEnemy[2][4] += sum / 8.0;
            }else if(x == 4){
                sum += toEnemy[1][4] * (1-rate1);
                toEnemy[1][4] = toEnemy[1][4] * rate1;

                toEnemy[0][3] += sum / 5.0;
                toEnemy[0][4] += sum / 5.0;
                toEnemy[1][3] += sum / 5.0;
                toEnemy[2][3] += sum / 5.0;
                toEnemy[2][4] += sum / 5.0;
            }

        }else if(y == 2){
            if(x == 0){
                sum += toEnemy[2][0] * (1 - rate1);
                toEnemy[2][0] = toEnemy[2][0] * rate1;

                toEnemy[1][0] += sum / 5.0;
                toEnemy[1][1] += sum / 5.0;
                toEnemy[2][1] += sum / 5.0;
                toEnemy[3][0] += sum / 5.0;
                toEnemy[3][1] += sum / 5.0;
            }else if(x == 1){
                sum += toEnemy[2][1] * (1-rate1);
                toEnemy[2][1] = toEnemy[2][1] * rate1;

                toEnemy[1][0] += sum / 8.0;
                toEnemy[1][1] += sum / 8.0;
                toEnemy[1][2] += sum / 8.0;
                toEnemy[2][0] += sum / 8.0;
                toEnemy[2][3] += sum / 8.0;
                toEnemy[3][0] += sum / 8.0;
                toEnemy[3][1] += sum / 8.0;
                toEnemy[3][2] += sum / 8.0;
            }else if(x == 2){
                sum += toEnemy[2][2] * (1-rate1);
                toEnemy[2][2] = toEnemy[2][2] * rate1;

                toEnemy[1][1] += sum / 8.0;
                toEnemy[1][2] += sum / 8.0;
                toEnemy[1][3] += sum / 8.0;
                toEnemy[2][1] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[3][1] += sum / 8.0;
                toEnemy[3][2] += sum / 8.0;
                toEnemy[3][3] += sum / 8.0;
            }else if(x == 3){
                sum += toEnemy[2][3] * (1-rate1);
                toEnemy[2][3] = toEnemy[2][3] * rate1;

                toEnemy[1][2] += sum / 8.0;
                toEnemy[1][3] += sum / 8.0;
                toEnemy[1][4] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[2][4] += sum / 8.0;
                toEnemy[3][2] += sum / 8.0;
                toEnemy[3][3] += sum / 8.0;
                toEnemy[3][4] += sum / 8.0;
            }else if(x == 4){
                sum += toEnemy[2][4] * (1-rate1);
                toEnemy[2][4] = toEnemy[2][4] * rate1;

                toEnemy[1][3] += sum / 5.0;
                toEnemy[1][4] += sum / 5.0;
                toEnemy[2][3] += sum / 5.0;
                toEnemy[3][3] += sum / 5.0;
                toEnemy[3][4] += sum / 5.0;
            }

        }else if(y == 3){
            if(x == 0){
                sum += toEnemy[3][0] * (1 - rate1);
                toEnemy[3][0] = toEnemy[3][0] * rate1;

                toEnemy[2][0] += sum / 5.0;
                toEnemy[2][1] += sum / 5.0;
                toEnemy[3][1] += sum / 5.0;
                toEnemy[4][0] += sum / 5.0;
                toEnemy[4][1] += sum / 5.0;
            }else if(x == 1){
                sum += toEnemy[3][1] * (1-rate1);
                toEnemy[3][1] = toEnemy[3][1] * rate1;

                toEnemy[2][0] += sum / 8.0;
                toEnemy[2][1] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[3][0] += sum / 8.0;
                toEnemy[3][2] += sum / 8.0;
                toEnemy[4][0] += sum / 8.0;
                toEnemy[4][1] += sum / 8.0;
                toEnemy[4][2] += sum / 8.0;
            }else if(x == 2){
                sum += toEnemy[3][2] * (1-rate1);
                toEnemy[3][2] = toEnemy[3][2] * rate1;

                toEnemy[2][1] += sum / 8.0;
                toEnemy[2][2] += sum / 8.0;
                toEnemy[2][3] += sum / 8.0;
                toEnemy[3][1] += sum / 8.0;
                toEnemy[3][3] += sum / 8.0;
                toEnemy[4][1] += sum / 8.0;
                toEnemy[4][2] += sum / 8.0;
                toEnemy[4][3] += sum / 8.0;
            }else if(x == 3){
                sum += toEnemy[3][3] * (1-rate1);
                toEnemy[3][3] = toEnemy[3][3] * rate1;

                toEnemy[2][2] += sum / 8.0;
                toEnemy[2][3] += sum / 8.0;
                toEnemy[2][4] += sum / 8.0;
                toEnemy[3][2] += sum / 8.0;
                toEnemy[3][4] += sum / 8.0;
                toEnemy[4][2] += sum / 8.0;
                toEnemy[4][3] += sum / 8.0;
                toEnemy[4][4] += sum / 8.0;
            }else if(x == 4){
                sum += toEnemy[3][4] * (1-rate1);
                toEnemy[3][4] = toEnemy[3][4] * rate1;

                toEnemy[2][3] += sum / 5.0;
                toEnemy[2][4] += sum / 5.0;
                toEnemy[3][3] += sum / 5.0;
                toEnemy[4][3] += sum / 5.0;
                toEnemy[4][4] += sum / 5.0;
            }

        }else if(y == 4){
            if(x == 0){
                sum += toEnemy[4][0] * (1 - rate1);
                toEnemy[4][0] = toEnemy[4][0] * rate1;

                toEnemy[3][0] += sum / 3.0;
                toEnemy[3][1] += sum / 3.0;
                toEnemy[4][1] += sum / 3.0;
            }else if(x == 1){
                sum += toEnemy[4][1] * (1-rate1);
                toEnemy[4][1] = toEnemy[4][1] * rate1;

                toEnemy[3][0] += sum / 5.0;
                toEnemy[3][1] += sum / 5.0;
                toEnemy[3][2] += sum / 5.0;
                toEnemy[4][0] += sum / 5.0;
                toEnemy[4][2] += sum / 5.0;
            }else if(x == 2){
                sum += toEnemy[4][2] * (1-rate1);
                toEnemy[4][2] = toEnemy[4][2] * rate1;

                toEnemy[3][1] += sum / 5.0;
                toEnemy[3][2] += sum / 5.0;
                toEnemy[3][3] += sum / 5.0;
                toEnemy[4][1] += sum / 5.0;
                toEnemy[4][3] += sum / 5.0;
            }else if(x == 3){
                sum += toEnemy[4][3] * (1-rate1);
                toEnemy[4][3] = toEnemy[4][3] * rate1;

                toEnemy[3][2] += sum / 5.0;
                toEnemy[3][3] += sum / 5.0;
                toEnemy[3][4] += sum / 5.0;
                toEnemy[4][2] += sum / 5.0;
                toEnemy[4][4] += sum / 5.0;
            }else if(x == 4){
                sum += toEnemy[4][4] * (1-rate1);
                toEnemy[4][4] = toEnemy[4][4] * rate1;

                toEnemy[3][3] += sum / 3.0;
                toEnemy[3][4] += sum / 3.0;
                toEnemy[4][3] += sum / 3.0;
            }
        }
    }

    public boolean canattackTE(int x, int y){
        if(getCoordinateTE(x, y) == 0){
            if(existarroundTE(x, y)){
                return true;
            }
        }
        return false;
    }

    public void befkousin(int x, int y){ //攻撃座標の更新
        befx = x;
        befy = y;
    }

    public boolean enemycanmove(int d, int n){
        if(d==0){
            if(befx+n >= 0 && befx+n <= 4 && befx+n >= 0 && befx+n <= 4){
                return true;
            }else{
                return false;
            }
        }else if(d==1){
            if(befx-n >= 0 && befx-n <= 4 && befx-n >= 0 && befx-n <= 4){
                return true;
            }else{
                return false;
            }
        }else if(d==2){
            if(befy+n >= 0 && befy+n <= 4 && befy+n >= 0 && befy+n <= 4){
                return true;
            }else{
                return false;
            }
        }else if(d==3){
            if(befy-n >= 0 && befy-n <= 4 && befy-n >= 0 && befy-n <= 4){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void dieTE(int x, int y){
        //System.out.println("dieTE");
        double sum = 0;
        int d = 0;
        sum = toEnemy[y][x];
        toEnemy[y][x] = 0;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(coordinateTE[j][i] == -1){
                    d++;
                }
            }
        }

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(i == x && j == y){
                    //何もしない
                }else{
                    if(coordinateTE[j][i] != -1){
                        toEnemy[j][i] += sum / (25 - d);
                    }
                }
            }
        }
    }

    public void kakuninTE(){ //デバッグ用
        System.out.print("\n\n<kakuninTE>    Max:  ");
        int n = 0;
        double max = -1;
        double sum = 0;
        for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                sum += toEnemy[j][i];
                if(max < toEnemy[j][i]){
                    max = toEnemy[j][i];
                }
            }
        }
        System.out.print(String.format("%.3f", max)+"  ");
        Max: for(int j=0; j<5; j++){
            for(int i=0; i<5; i++){
                if(max == toEnemy[j][i]){
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
                System.out.print("("+j+", "+i+") "+String.format("%.3f", toEnemy[j][i])+"  ");
            }
            System.out.println();
        }
        System.out.print("toEnemy sum: "+String.format("%.3f", sum)+"  ");
    }
}
