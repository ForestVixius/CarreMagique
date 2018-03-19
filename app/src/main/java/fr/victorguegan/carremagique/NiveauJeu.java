package fr.victorguegan.carremagique;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victorguegan.carremagique.R;


public class NiveauJeu extends AppCompatActivity implements View.OnClickListener {

    private EditText[] tabCases;
    private EditText[] tabEditText = new EditText[9];
    private Chronometer tempsDeJeu;

    private boolean niveauFini = false;
    private int score;
    private int numNiveau;
    private int nbIndiceUtilise = 0;
    private int[] tabChiffresCase;

    private TextView texteLigne1;
    private TextView texteColonne1;
    private TextView texteLigne2;
    private TextView texteColonne2;
    private TextView texteLigne3;
    private TextView texteColonne3;
    private int tps = 0;

    /**
     * Methode de création de la view
     * On va entre autre le mettre les actionlistener, mettre en place le chronomètre
     * remplir les tableau de EditView et des cases et enfin sauvegarde le tableau
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button boutonQuitter = findViewById(R.id.quitter);
        boutonQuitter.setOnClickListener(this);
        Button boutonEnvoyer = findViewById(R.id.envoyer);
        boutonEnvoyer.setOnClickListener(this);
        Button boutonRegles = findViewById(R.id.regles);
        boutonRegles.setOnClickListener(this);
        Button boutonIndice = findViewById(R.id.indice);
        boutonIndice.setOnClickListener(this);

        tempsDeJeu = findViewById(R.id.chronometer);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/lazer84.ttf");
        tempsDeJeu.setTypeface(font, Typeface.NORMAL);


        EditText case1 = findViewById(R.id.case1);
        EditText case2 = findViewById(R.id.case2);
        EditText case3 = findViewById(R.id.case3);
        EditText case4 = findViewById(R.id.case4);
        EditText case5 = findViewById(R.id.case5);
        EditText case6 = findViewById(R.id.case6);
        EditText case7 = findViewById(R.id.case7);
        EditText case8 = findViewById(R.id.case8);
        EditText case9 = findViewById(R.id.case9);

        tabCases = new EditText[]{case1, case2, case3, case4, case5, case6, case7, case8, case9};

        texteLigne1 = findViewById(R.id.resultatRow1);
        texteLigne2 = findViewById(R.id.resultatRow2);
        texteLigne3 = findViewById(R.id.resultatRow3);
        texteColonne1 = findViewById(R.id.resultatColumn1);
        texteColonne2 = findViewById(R.id.resultatColumn2);
        texteColonne3 = findViewById(R.id.resultatColumn3);
        tabEditText[0] = case1;
        tabEditText[1] = case2;
        tabEditText[2] = case3;
        tabEditText[3] = case4;
        tabEditText[4] = case5;
        tabEditText[5] = case6;
        tabEditText[6] = case7;
        tabEditText[7] = case8;
        tabEditText[8] = case9;

        numNiveau = getIntent().getIntExtra("niveau", 1);

        if (savedInstanceState == null || !savedInstanceState.containsKey("cases")) {
            creerPartie();
        }
        else {
            tps = savedInstanceState.getInt("tempsFini");
            tempsDeJeu.setBase(savedInstanceState.getLong("chrono"));
            tabChiffresCase = savedInstanceState.getIntArray("cases");
            niveauFini = savedInstanceState.getBoolean("nivFini");
            score = savedInstanceState.getInt("score");
            definirTexteCarre();
        }

        if (niveauFini) {
            afficherMessage();
        } else {
            tempsDeJeu.start();
        }

    }

    /**
     * Methode du actionlistener, pour le bouton :
     * regle on ouvre la page Wikipedia du carré magique
     * quitter on retourne au menu
     * envoyer on vérifie les données remplies dans le carré
     * indice on affiche une des cases du carré
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quitter:
                finish();
                break;

            case R.id.regles:
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://fr.wikipedia.org/wiki/Carr%C3%A9_magique_(math%C3%A9matiques)"));
                startActivity(viewIntent);
                break;

            case R.id.envoyer:
                verificationChiffres();
                break;

            case R.id.indice:
                nbIndiceUtilise++;
                afficherIndice();
                break;
        }

    }

    /**
     * Méthode qui affiche un des chiffres manquants au hasard en violet
     */
    private void afficherIndice() {
        int alea;
        for (int i = 0; i < tabCases.length; i++) {
            if (tabCases[i].getText().toString().equals("")) {
                tabCases[i].setText(tabChiffresCase[i] + "");
                tabCases[i].setTextColor(Color.rgb(56,0,108));
                break;
            }
        }
    }

    /**
     * Methode qui transforme le contenu d'un textview en int
     */
    private int textToInt( TextView text ) {
        return Integer.parseInt(text.getText().toString());
    }

    /**
     * Méthode pour vérifier si le tableau est bien rempli par l'utilisateur
     * si il y a une erreur le chrono clignote et augmente de 10s et un toast
     * indique à l'utilisateur qu'il s'est trompé
     */
    private void verificationChiffres() {
        boolean bOk = false;
        for (int i = 0; i < tabCases.length; i++) {if (tabCases[i].getText().toString().equals("")) return;}
        int[] value = new int[9];
        for (int i = 0; i < tabCases.length; i++) { value[i] = textToInt(tabCases[i]); }
        if (value[0] + value[1] + value[2] == textToInt(texteLigne1) ) {
            if (value[3] + value[4] + value[5] == textToInt(texteLigne2) ) {
                if (value[6] + value[7] + value[8] == textToInt(texteLigne3)) {
                    if (value[0] + value[3] + value[6] == textToInt(texteColonne1)) {
                        if (value[1] + value[4] + value[7] == textToInt(texteColonne2)) {
                            if (value[2] + value[5] + value[8] == textToInt(texteColonne3)) {
                                bOk = true;
                            }
                        }
                    }
                }
            }
        }
        if (bOk) {
            niveauFini = true;
            getScore();
            tps = (int) (SystemClock.elapsedRealtime() - tempsDeJeu.getBase() ) / 1000 ;
            tempsDeJeu.stop();
            afficherMessage();
        } else {
            Toast.makeText(this, "Vous avez fait une erreur ou plus !", Toast.LENGTH_LONG).show();
            clignoterChrono(Color.RED,5, 200);
            long tps = tempsDeJeu.getBase() - 10000;
            tempsDeJeu.setBase(tps);
            tempsDeJeu.start();
        }
    }

    /**
     * Methode pour afficher un alertdialog avec le score et le temps du joueur qd il a fini
     * et un bouton pour retourner au menu
     */
    private void afficherMessage() {
        clignoterChrono(Color.GREEN,20, 1000);
        AlertDialog messageGagne = new AlertDialog.Builder(NiveauJeu.this).create();
        messageGagne.setCancelable(false);
        messageGagne.setMessage("Bravo ! \nVoici votre score : " + score +"\nVous avez fini en : " + tps/60 +"min"+ tps%60 +"s");
        messageGagne.setButton(AlertDialog.BUTTON_POSITIVE, "Retour au choix de niveau", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); finish();
            }
        });
        messageGagne.show();
    }

    /**
     * Methode qui fait clignoter le chronometre, on choisi la couleur, le nb de fois, et la vitesse
     */
    private void clignoterChrono( int couleur, int nbFois, int vitesse ) {
        ObjectAnimator anim = ObjectAnimator.ofInt(tempsDeJeu, "textColor", Color.WHITE, couleur,
                Color.WHITE);
        anim.setDuration(200);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(nbFois);
        anim.start();
    }

    /**
     * Methode qui créer la partie avec le tableau de case et lance le chrono
     */
    public void creerPartie() {
        for (int i = 0; i < tabEditText.length; i++) {
            tabEditText[i].setText(""); tabEditText[i].setHint("?");
        }
        tempsDeJeu.setBase(SystemClock.elapsedRealtime());
        tempsDeJeu.start();
        tabChiffresCase = new int[9];
        int alea = 0;
        for (int i = 0; i < tabChiffresCase.length; i++) {
            boolean valide = false;
            while (!valide) {
                valide = true;
                alea = chiffreAleatoire()+1;
                for (int j = 0; j < tabChiffresCase.length; j++) {
                    if (alea == tabChiffresCase[j]) {
                        valide = false;
                        break;
                    }
                }
            }
            tabChiffresCase[i] = alea;
        }
        definirTexteCarre();
    }

    public void definirTexteCarre() {
        texteLigne1.setText   ((tabChiffresCase[0] + tabChiffresCase[1] + tabChiffresCase[2]) + "");
        texteLigne2.setText   ((tabChiffresCase[3] + tabChiffresCase[4] + tabChiffresCase[5]) + "");
        texteLigne3.setText   ((tabChiffresCase[6] + tabChiffresCase[7] + tabChiffresCase[8]) + "");
        texteColonne1.setText ((tabChiffresCase[0] + tabChiffresCase[3] + tabChiffresCase[6]) + "");
        texteColonne2.setText ((tabChiffresCase[1] + tabChiffresCase[4] + tabChiffresCase[7]) + "");
        texteColonne3.setText ((tabChiffresCase[2] + tabChiffresCase[5] + tabChiffresCase[8]) + "");

        int alea;
        for (int i = 0; i < 9 - numNiveau; i++) {
            boolean valide = false;
            while (!valide) {
                alea = chiffreAleatoire();
                if (tabCases[alea].getText().toString().equals("")) {
                    tabCases[alea].setText(tabChiffresCase[alea] + "");
                    valide = true;
                }
            }
        }
    }

    /**
     * métthode pour générer aléatoirement un chiffre entre 1 et 9;
     * @return
     */
    public int chiffreAleatoire() {
        return (int) (Math.random() * (9));
    }

    /**
     * On sauvegarde les chiffres dans le tableau de cases, le temps du chrono, ...
     * pour la rotation de l'écran
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("nivFini", this.niveauFini);
        savedInstanceState.putIntArray("cases", tabChiffresCase);
        savedInstanceState.putLong("chrono", tempsDeJeu.getBase());
        if (niveauFini) { savedInstanceState.putInt("tempsFini", tps );}
        savedInstanceState.putInt("score", this.score);
    }

    /**
     * Méthode avec une formule super secrète et obscure afin d'obtenir le score du joueur
     * @return
     */
    public void getScore() {
        int tempsJoue = (int) (SystemClock.elapsedRealtime() - tempsDeJeu.getBase() ) / 1000;
        this.score = ( 1000 + (numNiveau-1) * 200 - nbIndiceUtilise * 100 * (2*numNiveau) - ( tempsJoue*2 ) + tempsJoue*11 );
        if(score < 0 ) score  = 0;
    }
}
