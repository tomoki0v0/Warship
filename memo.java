//Wqrship getmax
public void myTurn(){
	int cando = 0;
	int movemove = 0;
	kousin();
	junjokousin();

	for(int i = 0; i < 5; i++){
		for(int j =0; j < 5; j++){
			if(enemyPredict.getEnemyPredict(i, j) >= 30 && getCoordinate(i, j) == 1){
				movemove = 1;
			}
		}
	}
	if(movemove = 1 && cando == 0){ //移動
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
			maxCount = 0;
			for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					if(toEnemy.getmax(k) == toEnemy.getToEnemy(x, y)){
						maxCount++; //Maxを数える
					}
				}
			}
			middle:while(0 < maxCount){
				if(maxCount > 1){
					in:while(true){
						int ii;
						int jj;
						ii = Math.random(5);
						jj = Math.random(5);
						if(confirm[jj][ii] = 0){
							if(toEnemy.getmax(k) == toEnemy.getToEnemy(ii,jj)){
								if(toEnemy.canattackTE(ii, jj)){
									syori(jj*10+ii);
									cando = 1;
									break out;
								}else{
									maxCount--;
									cycle++;
									confirm[jj][ii] = 1;
									break in;
								}
							}
						}
					}//while in
				}else if(maxCount = 1){
					for(int i = 0; i < 5; i++){
						for(int j = 0; j < 5; j++){
							if(confirm[j][i] == 0 && toEnemy.getToEnemy(j, i) == toEnemy.getmax(k)){
								if(toEnemy.canAttack(j, i){
									//jiに砲撃
									cando = 1;
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

	if(cando = 0){ //強制移動
		move();
		cando = 1;
	}

	if(cando == 0){
		System.out.println("動作を完了していません。例外が発生しました。");
		System.exit(0);
	}
}

//Warship
public void move(){
	int cycle = 0;
	int k = 0;
	int n;
	int[][] confirm = new int[5][5]; //確認用の配列
	for(int i = 0; i < 5; i++){
		for(int j = 0; j < 5; j++){
			confirm[y][x] = 0; //初期化
		}
	}

	while(k < 4){
		int mv1 = -1;
		int mv2 = -1;
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if(enemyPredict.canmove(i, j) && enemyPredict.getshipjunjo(k) == enemyPredict.getEnemyPredict(i, j)){
					mv1 = j + 10 + i;
				}
			}
		}
		if(mv1 != -1 && mv2 == -1){
			if(hant == 0){
				if(Math.random() < 0.3){ //ブラフ
					mv2 = enemyPredict.bluffnosyori(mv1);
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
				if(n == 2 && mode != 0){
					enemyPredict.runme(mv1, mv2);
					System.out.println("行動：ブラフ");
				}else if(n == 1){
					enemyPredict.runme(mv1, mv2);
					System.out.println("行動：逃亡");
				}else{
					enemyPredict.moveme(mv1, mv2);
					System.out.println("行動：通常移動");
				}
				break;
			}
		}else{
			k++;
		}
	} //while

	//強制移動
	System.out.println("移動できませんでした。");
	System.exit(0);
}

//ToEnemy
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
			if(i >= 0 && i <= 4 && j >= 0 && j <= 4　&& coordinateTE[j][i] != -1){
				sum += toEnemy[j][i] * (1-rate4);
				toEnemy[j][i] = toEnemy[j][i] * rate4;
				d++;
			}
		}
	}

	for(int i = 0; i < 5; i++){
		for(int j = 0; j < 5; j++){
			if(i >= 0 && i <= 4 && j >= 0 && j <= 4){
				if(x-2 < i < x+2 && y-2 < y+2){
					//何もしない
				}else if{
					if(coordinateTE[j][i] != -1){
						toEnemy[j][i] += sum / (25-d);
					}
				}
			}
		}
	}
}

//toEnemy
public void teHit(int x, int y){
	//System.out.println("teHit X: "+x+", Y: "+y);
	sum = 0.0;
	for(int j = y-1; j < y+2; j++){
		for(int i = x-1; i < x+2; i++){
			if(j == y && i == x){
				//何もしない
			}else if{
				if(getCoordinateTE(i, j) != -1){
					sum += toEnemy[j][i] * rate0;
					toEnemy[j][i] *= (1 - rate0);
				}
			}
		}
	}

	for(int j = 0; j < 5; j++){
		for(int i = 0; i < 5; i++){
			if(x-2 < i < x+2 && y-2 < j < y+2){
			}else if{
				if(getCoordinateTE(i, j) != -1){
					sum += toEnemy[j][i] * rate00;
					toEnemy[j][i] *= (1 - rate00);
				}
			}
		}
	}
	toEnemy[y][x] += sum;
}
