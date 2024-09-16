
document.getElementById('salva-formazione-btn').addEventListener('click', function() {
	const giocatoriDaInviare = [];

	const titolari = document.querySelectorAll('#titolari-list .giocatore');
	titolari.forEach(giocatore => {
		const hiddenInput = giocatore.querySelector('input[type="hidden"]');
		 if (hiddenInput) {
            const idGiocatore = hiddenInput.value;
            giocatoriDaInviare.push({
			id: idGiocatore,
			titolare: true
		});
        } else {
            console.error('Input hidden non trovato per il giocatore:', giocatore);
        }
	});

	const riserve = document.querySelectorAll('#riserve-list .giocatore');
	riserve.forEach(giocatore => {
		 const hiddenInput = giocatore.querySelector('input[type="hidden"]');
		 
        if (hiddenInput) {
            const idGiocatore = hiddenInput.value;
            giocatoriDaInviare.push({
			id: idGiocatore,
			titolare: false
		});
        } else {
            console.error('Input hidden non trovato per il giocatore:', giocatore);
        }
		
	});

	const squadraId = document.getElementById('squadra-id').value;

	console.log(giocatoriDaInviare);

	fetch('/presidente/squadra/' + squadraId + '/salva_formazione', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(giocatoriDaInviare)
	})
		.then(response => response.text())
		.then(data => {
				UIkit.notification({ message: 'Formazione salvata con successo', status: 'success' });
		})
		.catch(error => {
			UIkit.notification({ message: 'Errore di comunicazione col server', status: 'danger' });
			console.error('Errore:', error);
		});
});
