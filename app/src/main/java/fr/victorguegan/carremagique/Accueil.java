package fr.victorguegan.carremagique;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.victorguegan.carremagique.R;

public class Accueil extends AppCompatActivity implements View.OnClickListener{

    private Button boutonPartie;
    private Button boutonQuitter;

    /**
     * Methode de création de la View d'accueil
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        boutonPartie =  findViewById(R.id.accueil_nouvellePartie);
        boutonQuitter = findViewById(R.id.accueil_quitter);
        boutonPartie.setOnClickListener(this);
        boutonQuitter.setOnClickListener(this);
    }

    /**
     * Methode du ClickListenner, on va au choix du niveau si on est pas dans le fragment de niveau
     * sinon ça veut dire qu'on est sur le choix du niveau et on lance le jeu.
     * @param view on récupère la view actuelle
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.accueil_nouvellePartie ) {
            transitionChoixNiveau(new ChoixDifficulte());
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    /**
     * Methode pour ouvrir le fragment avec une transition de fade.
     * @param f on récupère le nouveau fragment
     */
    private void transitionChoixNiveau(Fragment f) {
        final Fragment fragment = f;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.activity_home, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
            }
        };
        handler.postDelayed(runnable, 250);
    }

}
