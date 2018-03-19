package fr.victorguegan.carremagique;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.victorguegan.carremagique.R;


public class ChoixDifficulte extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner selection;
    private int numNiveau = 1;
    private Button lancerPartie;

    public ChoixDifficulte() {
        // Required empty public constructor
    }

    /**
     * Methode pour créer le fragment du choix de niveau
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choix_difficulte, container, false);
        supprimerBoutonsAccueil(view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.tab_niveaux, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selection.setAdapter(adapter);
        selection.setOnItemSelectedListener(this);
        lancerPartie.setOnClickListener(this);
        return view;
    }

    /**
     * Méthode pour faire disparaitre les boutons de la view d'accueil
     * pour ne pas qu'ils cachent le spinner et le bouton de lancement
     * @param view on récupère la view d'accueil
     */
    private void supprimerBoutonsAccueil(View view) {
        Button boutonPartie = getActivity().findViewById(R.id.accueil_nouvellePartie);
        Button boutonQuitter =  getActivity().findViewById(R.id.accueil_quitter);
        lancerPartie = view.findViewById(R.id.lancer);
        selection = view.findViewById(R.id.selection);
        if(boutonPartie != null) { boutonPartie.setVisibility(View.INVISIBLE); boutonQuitter.setVisibility(View.INVISIBLE);}
    }

    /**
     * Methode du ClickListener, qd on click sur
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.lancer) {
            Intent myIntent = new Intent(getContext(), NiveauJeu.class);
            myIntent.putExtra("niveau", numNiveau);
            startActivity(myIntent);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        numNiveau = Integer.parseInt((String) selection.getItemAtPosition(i));
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        numNiveau = 1;
    }
}
