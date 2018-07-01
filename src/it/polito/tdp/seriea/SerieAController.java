/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSquadra"
    private ChoiceBox<Team> boxSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelezionaSquadra"
    private Button btnSelezionaSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaAnnataOro"
    private Button btnTrovaAnnataOro; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaCamminoVirtuoso"
    private Button btnTrovaCamminoVirtuoso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    

	public void setModel(Model model) {
		this.model=model;
		boxSquadra.getItems().addAll(model.getSquadre());
	}
	
    @FXML
    void doSelezionaSquadra(ActionEvent event) {

    	Team team=boxSquadra.getValue();
    	if(team==null) {
    		txtResult.setText("Selezionare una squadra!!");
    		return;
    	}
    	
    	
    	txtResult.setText("Punti squadra "+team+" nelle varie stagioni");
    	for(Season s: model.puntiClassifica(team)) {
    		txtResult.appendText("\n"+s.toString());
    	}
    	
    	
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {

    	try {
    	model.creaGrafo();
    	
    	//l’annata d’oro per la squadra selezionata, definita come la stagione nella
    	Team team=boxSquadra.getValue();
    	Season oro=model.annataOro();
    	txtResult.appendText("\n\nAnnata d'oro per la "+team+" fu: "+oro.getDescription()+" con punti:"+oro.getAnnataDoro());
    	} catch(RuntimeException e) {
    		txtResult.setText("Errore creazione grafo, prima selezionare squadra!!!");
    	}
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    //serie di stagioni consecutive nelle quali la squadra abbia sempre migliorato il punteggio rispetto alla stagione precedente.
    model.getCammino();
    List<Season> cammino= model.getBestCammino();
    
    txtResult.appendText(String.format("\n\nCammino virtuoso di dimensione: %d", cammino.size()));
    if(cammino.isEmpty()) {
		txtResult.setText("Nessun cammino trovato!");
		return;
	}
    for(Season s: cammino) {
		txtResult.appendText("\n"+s.toString());
	}
    
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

}
