document.addEventListener('DOMContentLoaded', function () {
    const salvaFormazioneBtn = document.getElementById('salva-formazione-btn');

    if (salvaFormazioneBtn) {
        salvaFormazioneBtn.addEventListener('click', function (event) {
            event.preventDefault();

            const titolariList = document.querySelectorAll('#titolari-list .giocatore');
            const riserveList = document.querySelectorAll('#riserve-list .giocatore');

            if (titolariList.length > 5) {
                alert('Errore: non puoi avere più di 5 giocatori titolari.');
                return;
            }

            updatePlayersList(titolariList, "true");
            updatePlayersList(riserveList, "false");

            document.getElementById('salvaFormazione').submit();
        });
    }

    function updatePlayersList(players, isTitolare) {
        players.forEach(function (player) {
            const titolaritaInput = player.querySelector('input.titolarita');
            if (titolaritaInput) {
                titolaritaInput.value = isTitolare;
            }
        });
    }
});
