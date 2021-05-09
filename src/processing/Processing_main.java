package processing;

import processing.core.PApplet;

public class Processing_main extends PApplet {

    private Game newGame;

    private int black = color(0, 0, 0);
    private int white = color(255, 255, 255);
    private int red = color(255, 0, 0);
    private int gold = color(150, 150, 0);

    private int backgroundColor = this.white;
    private int stringColor = this.black;
    private int btnBackgroundColor = this.black;
    private int btnStringColor = this.white;
    private int readyStringColor = this.red;
    private int pointerColor = this.black;
    private int narrowFieldColor = this.black;
    private int gameOverBackgroundColor = this.black;
    private int gameOverStringColor = this.white;
    private int gameClearBackgroundColor = this.white;
    private int gameClearStringColor = this.gold;

    private int oneSecond = 1000;

    public void setup() {
        // 描画領域
        
        // size(1000, 1000);

        // noStroke()は移動

        // 一秒間のdraw()の描画回数
        frameRate(60);

        // second の加算は消去し初期値として代入

        // ボールの直径、初期座標、進行速度を決める
        //decideBallStandard();
        this.newGame = new Game("DEBUG");
    }

    public void draw() {
        this.newGame.run();
    }

    // Enterキーを押すと終了
    public void keyPressed() {
        if(key == ENTER) {
            exit();
        }
    }

    // start_scene      起動画面
    // first_scene      最初の画面
    // explain_scene    説明の画面
    // stLevel_scene    standardモードのレベル選択画面
    // adLevel_scene    advanceモードのレベル選択画面
    // stGame_scene     standardモードのゲーム実行画面
    // adGame_scene     advanceモードのゲーム実行画面
    // gameOver_scene   ゲームオーバーの画面
    // gameClear_scene  ゲームクリアの画面
    // variable_scene   変数集合(?)
    // function_scene   関数集合(?)
    // wait_scene       待機画面
    // startGame_scene  ゲームの開始画面
    
    public class Game{
        // ボールの数
        private int ballcnt;
        // 直径
        private final int[] ballDiameter = new int[12];
        // 初期座標x
        private final int[] ballXs = new int[12];
        // 初期座標y
        private final int[] ballYs = new int[12];
        // 進行速度x
        private final int[] stepXs = new int[12];
        // 進行速度y
        private final int[] stepYs = new int[12];

        // 画面遷移のための変数
        // 今の画面
        public String scene = "start";
        // ひとつ前の画面
        private String prevScene = "";
        // scene又はprevSceneに格納できる文字列(draw関数内で判定)
        // start
        // first
        // stLevel
        // adLevel
        // standard
        // advance
        // over
        // clear

        // ユーザーの入力に応じて、レベルを代入
        // private String st = "";
        // private String ad = "";
        // one
        // two
        // three

        // 現在の時間を格納する変数
        private int time;
        // 経過時間を格納する変数
        private int difTime;

        // カウントダウン画面用変数
        // private int i;

        // ボールの色(RGB)
        private final int[] ballColors = {
            color(200, 100,  10),   // 茶色
            color( 10, 200, 100),   // エメラルド
            color( 10, 100, 200),   // 青
            color(100, 100, 100),   // 灰色
            color(200, 200, 200),   // 薄い灰色
            color(200, 200, 100),   // 薄い黄土色
            color(200, 100, 200),   // 薄い紫
            color(100, 200, 200),   // 水色
            color(200,  10, 100),   // 恋ピンク
            color(100,  10, 200),   // 紫
            color(150, 150,   0),   // 金色
            color(120, 120, 120),   // 灰色
        };

        // 最初のカウント画面
        // ptivate final int[] cnt = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        // ダメージの数が 3 ( 合計 20 分の 1 秒間分)入るとゲームオーバー ( 人間の感覚からすると一瞬)
        private int damagecnt;

        // 描画開始からの秒数 ( 10 秒で 1 カウント )
        // private int tenSecondElapsed = 1;

        private boolean isRelease;

        // コンストラクター
        public Game(String condition) {
            this.isRelease = condition == "RELEASE" ? true : false;
        }

        // 画面更新時に必ず背景を白色でリセットする
        public void init() {
            background(255);

            // 左上にデバッグ文字列を表示
            if (!this.isRelease) {
                textSize(20);
                fill(stringColor);
                text("ballcnt: " + str(this.ballcnt), 100, 50);
                text("scene: " + this.scene, 100, 100);
                text("prevScene: " + this.prevScene, 100, 150);
                text("time: " + str(this.time), 100, 200);
                text("difTime: " + str(this.difTime), 100, 250);
                text("damagecnt: " + str(this.damagecnt), 100, 300);
            }
        }

        public void run() {
            this.init();
            if(this.scene == "start")     this.start_scene();
            if(this.scene == "first")     this.first_scene();
            // if(this.scene == "explain")   this.explain_scene();
            if(this.scene == "stLevel")   this.stLevel_scene();
            if(this.scene == "adLevel")   this.adLevel_scene();
            if(this.scene == "standard")  this.stGame_scene();
            if(this.scene == "advance")   this.adGame_scene();
            if(this.scene == "over")      this.gameOver_scene();
            if(this.scene == "clear")     this.gameClear_scene();
            // if(this.scene == "wait")      this.wait_scene();
            if(this.scene == "startGame") this.startGame_scene();
        }

        // 起動画面
        public void start_scene() {
            if(this.prevScene == "") {
                println("GET / 304  start_scene is true");
                // 初回アクセス時のみ現在の時間を記録
                this.time = millis();
                // 初回アクセス時以外は実行しないよう、変数を更新
                this.prevScene = "true";
            }
            // 繰り返しごとに初回アクセス時からの経過時間を確認
            this.difTime = millis() - this.time;

            // テキストを描画
            textSize(40);
            textAlign(CENTER, CENTER);
            fill(stringColor);
            text("Loading...", width/2, height/16*13);

            // 長方形を描画
            noStroke();
            rectMode(CENTER);
            fill(btnBackgroundColor);
            rect(width/2, height/8*7, width-50, 40);
            // fill(banStringColor);
            // rect(width/2, height/8*7, width-60, 30);

            // 経過時間が5秒を越したら画面遷移
            if(this.difTime >= oneSecond * 5) {
                this.prevScene = "start";
                this.scene = "first";
            }
        }

        // ゲームメニュー(最初の画面)
        public void first_scene() {
            // 遷移元は起動画面、ゲームクリア画面、ゲームオーバー画面のどれか
            if(this.prevScene == "start" || this.prevScene == "over" || this.prevScene == "clear") {
                println("GET / 304  first_scene is true");
                // 初回アクセス時以外は実行しないよう、変数を更新
                this.prevScene = "true";
            }

            // タイトルの描画
            textSize(200);
            textAlign(CENTER, CENTER);
            fill(stringColor);
            text("Game", width/2, height/3);

            // ボタンを描画
            noStroke();
            rectMode(CENTER);
            fill(btnBackgroundColor);
            rect(width/4, height/3*2, 180, 80);
            rect(width/4*3, height/3*2, 180, 80);

            // ボタンに文字を描画
            textSize(40);
            fill(btnStringColor);
            text("Standard", width/4*1, height/3*2);
            fill(stringColor);
            text("<< Select Mode >>", width/4*2, height/3*2);
            fill(btnStringColor);
            text("Advance", width/4*3, height/3*2);

            // マウスがクリックされたときに判定
            if(mousePressed) {
                // クリック場所が左側のボタンの範囲内だった場合、standardモードのレベル選択画面へ遷移
                if((mouseX >= width/4-90 && mouseX <= width/4+90) && (mouseY >= height/3*2-40 && mouseY <= height/3*2+40)) {
                    this.prevScene = "first";
                    this.scene = "stLevel";
                }
                // クリック場所が右側のボタンの範囲内だった場合、advanceモードのレベル選択画面へ遷移
                if((mouseX >= width/4*3-90 && mouseX <= width/4*3+90) && (mouseY >= height/3*2-40 && mouseY <= height/3*2+40)) {
                    this.prevScene = "first";
                    this.scene = "adLevel";
                }
            }
        }

        // standardモードのレベル選択画面
        public void stLevel_scene() {
            if(this.prevScene == "first") {
                println("GET / 304  stLevel_scene is true");
                this.prevScene = "true";
            }

            // ボタンを3つ描画
            noStroke();
            fill(btnBackgroundColor);
            rectMode(CENTER);
            rect(width/2, height/6*2, width/2, 100);
            rect(width/2, height/6*3, width/2, 100);
            rect(width/2, height/6*4, width/2, 100);

            // ボタンに文字列を描画
            textSize(40);
            fill(btnStringColor);
            text("1", width/2, height/6*2);
            text("Comming soon!", width/2, height/6*3);
            text("Comming soon!", width/2, height/6*4);

            // その他の文字列を描画
            fill(stringColor);
            text("Standard Mode", width/2, height/6*1);
            text("--------------------", width/2, height/24*5);
            text("^^^ Choice Game Level ^^^", width/2, height/6*5);

            // マウスがクリックされたときに判定
            if(mousePressed) {
                // クリック場所が一番上のボタンの範囲内だった場合、standardモードのレベル1のゲーム画面へ遷移
                if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*2-50 && mouseY <= height/6*2+50)) {
                    // this.st = "one";
                    this.prevScene = "stLevel";
                    this.scene = "startGame";
                }
                // クリック場所が真ん中のボタンの範囲内だった場合、standardモードのレベル2のゲーム画面へ遷移
                // if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*3-50 && mouseY <= height/6*3+50)) {
                //     this.st = "two";
                //     this.prevScene = "stLevel";
                //     this.scene = "startGame";
                // }
                // クリック場所が一番下のボタンの範囲内だった場合、standardモードのレベル3のゲーム画面へ遷移
                // if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*4-50 && mouseY <= height/6*4+50)) {
                //     this.st = "three";
                //     this.prevScene = "stLevel";
                //     this.scene = "startGame";
                // }
            }
        }

        // advanceモードのレベル選択画面
        public void adLevel_scene() {
            if(this.prevScene == "first") {
                println("GET / 304  adLevel_scene is true");
                this.prevScene = "true";
            }

            // ボタンを3つ描画
            noStroke();
            fill(btnBackgroundColor);
            rectMode(CENTER);
            rect(width/2, height/6*2, width/2, 100);
            rect(width/2, height/6*3, width/2, 100);
            rect(width/2, height/6*4, width/2, 100);

            // ボタンに文字列を描画
            textSize(40);
            fill(btnStringColor);
            text("1", width/2, height/6*2);
            text("Comming soon!", width/2, height/6*3);
            text("Comming soon!", width/2, height/6*4);

            // その他の文字列を描画
            fill(stringColor);
            text("Advance Mode", width/2, height/6*1);
            text("--------------------", width/2, height/24*5);
            text("^^^ Choice Game Level ^^^", width/2, height/6*5);

            // マウスがクリックされたときに判定
            if(mousePressed) {
                // クリック場所が一番上のボタンの範囲内だった場合、advanceモードのレベル1のゲーム画面へ遷移
                if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*2-50 && mouseY <= height/6*2+50)) {
                    // this.ad = "one";
                    this.prevScene = "adLevel";
                    this.scene = "startGame";
                }
                // クリック場所が真ん中のボタンの範囲内だった場合、advanceモードのレベル2のゲーム画面へ遷移
                // if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*3-50 && mouseY <= height/6*3+50)) {
                //     this.ad = "two";
                //     this.prevScene = "adLevel";
                //     this.scene = "startGame";
                // }
                // クリック場所が一番下のボタンの範囲内だった場合、advanceモードのレベル3のゲーム画面へ遷移
                // if((mouseX >= width/2-width/2/2 && mouseX <= width/2+width/2/2) && (mouseY >= height/6*4-50 && mouseY <= height/6*4+50)) {
                //     this.ad = "three";
                //     this.prevScene = "adLevel";
                //     this.scene = "startGame";
                // }
            }
        }

        // ゲームの開始画面
        public void startGame_scene() {
            if(this.prevScene == "stLevel" || this.prevScene == "adLevel") {
                //i = 10;
                println("GET / 304  startGame_scene is true");
                time = millis();
                if(this.prevScene == "stLevel") {
                this.prevScene = "true_st";
                }
                if(this.prevScene == "adLevel") {
                this.prevScene = "true_ad";
                }
            }
            this.difTime = millis() - this.time;

            // タイトルを描画
            drawCurrentTitle();

            // マウスポインタの黒い円を描画
            drawMousePointer();

            // 5秒経過したらそれぞれのゲーム画面に遷移
            if(this.difTime >= oneSecond * 5) {
                if(this.prevScene == "true_st") {
                this.scene = "standard";
                this.prevScene = "startGame";
                }
                if(this.prevScene == "true_ad") {
                this.scene = "advance";
                this.prevScene = "startGame";
                }
            }
        }

        // タイトル表示
        private void drawCurrentTitle() {
            drawTitle();
            //countdown();
        }

        // カウントダウン画面の「Ready ? 」表示
        private void drawTitle() {
            // textAlign()を使用し、「Ready?」を中央に配置
            // text("Ready?", 150, 300);
            // 実数を指定するのではなく相対的に値を指定する
            fill(readyStringColor);
            textSize(200);
            textAlign(CENTER,CENTER);
            text("Ready?", width/2, height/3);
            if(this.difTime >= oneSecond * 3.5f) {
                fill(stringColor);
                text("Go!", width/2, height/3*2);
            }
        }

        // カウントダウンの数字の表示
        // private void countdown() {
        //     while (i > 0) {
        //         // textAlign()を使用し、「Ready?」を中央に配置
        //         // text(cnt[j], 400, 600);
        //         // 実数を指定するのではなく相対的に値を指定する
        //         fill(0);
        //         textSize(200);
        //         textAlign(CENTER,CENTER);
        //         text(i, width/2, height/3*2);

        //         if(this.difTime >= oneSecond * 1) {
        //             i--;
        //         }
        //     }
        // }

        // マウスポインタの黒い円
        private void drawMousePointer() {
            fill(pointerColor);
            ellipse(mouseX, mouseY, 50, 50);
        }

        // standardモードのゲーム実行画面
        public void stGame_scene() {
            if(this.prevScene == "startGame") {
                decideBallStandard();
                this.ballcnt = 0;
                this.damagecnt = 0;
                println("GET / 304  standard_scene is true");
                this.time = millis();
                this.prevScene = "true_st";
            }
            this.difTime = millis() - this.time;
            // if 文は消去しnoloop()を実行する

            // 10秒たったらボールを追加する
            // if (this.difTime >= oneSecond * 10) {
            if (frameCount % 600 == 0) { // !!!
                this.ballcnt++;
                //tenSecondElapsed++;
            }

            // draw関数のボールに関する処理
            drawCurrentPosition();

            drawMousePointer();

            judgeDamege();

            // ゲームの判定
            judgementGame();
        }

        // advanceモードのゲーム実行画面
        public void adGame_scene() {
            if(this.prevScene == "startGame") {
                decideBallStandard();
                this.ballcnt = 0;
                this.damagecnt = 0;
                println("GET / 304  advance_scene is true");
                this.time = millis();
                this.prevScene = "true_ad";
            }
            this.difTime = millis() - this.time;
            // if 文は消去しnoloop()を実行する

            // advanceモード独自の黒画面を表示(narrow: 狭い)
            narrowScreen();

            // 10秒たったらボールを追加する
            // if (this.difTime >= oneSecond * 10) {
            if (frameCount % 600 == 0) { // !!!
                this.ballcnt++;
                //tenSecondElapsed++;
            }

            // draw関数のボールに関する処理
            drawCurrentPosition();

            drawMousePointer();

            judgeDamege();
            judgeNarrow();

            // ゲームの判定
            judgementGame();
        }

        // ボールの直径、初期座標、進行速度を決める
        private void decideBallStandard() {
            for(int i = 0; i < ballDiameter.length; i++) {
            this.ballDiameter[i] = PApplet.parseInt(random(100, 150));
            // ballXs[ballcnt] = int(random(200, 201));
            // ballYs[ballcnt] = int(random(200, 201));
            this.ballXs[i] = 75;
            this.ballYs[i] = 75;
            this.stepXs[i] = PApplet.parseInt(random(6, 8));
            this.stepYs[i] = PApplet.parseInt(random(3, 7));
            }
        }

        // draw 関数のボールに関する処理
        private void drawCurrentPosition() {
            for (int i = 0; i < this.ballcnt; i++) {
                changeDirection(i);
                movePosition(i);
                draw(i);
            }
        }

        // ボールに当たるとダメージを受ける、その時の処理
        private void judgeDamege() {
            // draw 関数が繰り返すごとに現在表示されているボール一つ一つとの距離を計算する
            for (int i = 0; i < this.ballcnt; i++) {
                // dist(x1, y1, x2, y2)  : 2 点間の距離を求める processing の組み込み関数
                if (dist(this.ballXs[i], this.ballYs[i], mouseX+30, mouseY+30) <= this.ballDiameter[i]/2) {
                    this.damagecnt++;
                }
            }
        }

        // ゲームの判定
        private void judgementGame() {
            if (this.damagecnt >= 3) {
                //scene = "over";
                if(this.prevScene == "true_st") this.prevScene = "standard";
                if(this.prevScene == "true_ad") this.prevScene = "advance";
                this.scene = "over";
            }
            if (this.difTime >= oneSecond * 120) {
                //scene = "clear";
                if(this.prevScene == "true_st") this.prevScene = "standard";
                if(this.prevScene == "true_ad") this.prevScene = "advance";
                this.scene = "clear";
            }
        }


        // ボールが壁に当たった時のバウンドの処理
        private void changeDirection(int i) {
            if (this.ballXs[i] < this.ballDiameter[i]/ 2 || this.ballXs[i] > width - this.ballDiameter[i] / 2) {
                this.stepXs[i] *= -1;
            }
            if (this.ballYs[i] < this.ballDiameter[i] / 2 || this.ballYs[i] > height - this.ballDiameter[i] / 2) {
                this.stepYs[i] *= -1;
            }
        }

        // ボールを移動させる処理
        private void movePosition(int i) {
            this.ballXs[i] += this.stepXs[i];
            this.ballYs[i] += this.stepYs[i];
        }

        // ボールの描画
        private void draw(int i) {
            noStroke();
            fill(this.ballColors[i]);
            ellipse(this.ballXs[i], this.ballYs[i], this.ballDiameter[i], this.ballDiameter[i]);
        }

        private void narrowScreen() {
            fill(narrowFieldColor);
            rectMode(CORNERS);
            rect(0, 0, 80, height);              // 左
            rect(0, 0, width, 80);               // 上
            rect(width-80, 0, width, height);    // 右
            rect(0, height-80, width, height);   // 下
            rectMode(CENTER);
        }

        private void judgeNarrow() {
            if((mouseX >= 0 && mouseX <= 80) && (mouseY >= 0 && mouseY <= height)) {                // 左
                this.damagecnt++;
            }
            if((mouseX >= 0 && mouseX <= width) && (mouseY >= 0 && mouseY <= 80)) {                 // 上
                this.damagecnt++;
            }
            if((mouseX >= width-80 && mouseX <= width) && (mouseY >= 0 && mouseY <= height)) {      // 右
                this.damagecnt++;
            }
            if((mouseX >= 0 && mouseX <= width) && (mouseY >= height-80 && mouseY <= height)) {     // 下
                this.damagecnt++;
            }
        }

        // ゲームオーバーの画面
        public void gameOver_scene() {
            if(this.prevScene == "standard" || this.prevScene == "advance") {
                println("GET / 304  over_scene is true");
                this.time = millis();
                this.prevScene = "true";
            }
            this.difTime = millis() - this.time;

            // ダメージが3回( 20分の1秒間分 )入った場合はゲームオーバー
            background(gameOverBackgroundColor);
            fill(gameOverStringColor);
            textSize(100);
            textAlign(LEFT);
            text("Game Over!", 10, 500);
            // noLoop() : draw()のループ処理を止める
            //noLoop();

            if(this.difTime >= oneSecond * 10) {
                this.prevScene = "over";
                this.scene = "first";
            }
        }

        // ゲームクリアの画面
        public void gameClear_scene() {
            if(this.prevScene == "standard" || this.prevScene == "advance") {
                println("GET / 304  clear_scene is true");
                this.time = millis();
                this.prevScene = "true";
            }
            this.difTime = millis() - this.time;

            background(gameClearBackgroundColor);
            fill(gameClearStringColor);
            textSize(100);
            textAlign(LEFT);
            text("Game Clear!", 10, 500);
            //noLoop();

            if(this.difTime >= oneSecond * 10) {
                this.prevScene = "clear";
                this.scene = "first";
            }
        }
    }

    public void settings() {
        fullScreen();
    }

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "MAIN" };
        PApplet.runSketch(appletArgs, new Processing_main());
    }

}
