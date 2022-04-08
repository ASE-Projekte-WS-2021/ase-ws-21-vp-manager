# VP-Manager

Der VP-Manager unterstützt die Studierenden der Medieninformatik und Informationswissenschaft beim Sammeln von Versuchspersonen-Stunden und bei der Suche nach 
Teilnehmer:innen für eigene Studien. Der eigene Account ist dabei an eine persönliche Mailadresse gebunden um die Nutzer eindeutig identifizieren zu können.

Die App gibt einen Überblick über den eigenen VP-Status, indem sie beispielsweise anstehene Termine oder auch bereits vom Sekretariat verbuchte VP-Stunden anzeigt. Die insgesamt
benötigten VP-Stunden können zudem angepasst werden.

Im Bereich "Studien finden" werden alle Einträge für verfügbare Studien aufgelistet, die in der App erstellt wurden. Diese können nach der Anzahl der VP-Stunden sortiert
und nach verschiedenen Kategorien gefiltert werden. Zu einer Studie werden viele nützliche Informationen bereitgestellt, wie beispielsweise eine Kontaktmöglichkeit zur 
Studienleitung oder ein ausführlicher Beschreibungstext. Außerdem kann zwischen Terminen gewählt werden, die zur Anmeldung zur Verfügung stehen.

Im Bereich "Studien erstellen" hat man die Möglichkeit seine eigenen Studien zu erstellen und anderen Nutzern die Teilnahme anzubieten. Der VP-Manager begleitet den 
Nutzer beim Anlegen der eigenen Studie, damit das Ergebnis ein möglichst vollständiger und aussagekräftiger Eintrag ist.

Desweiteren können in der App Informationen zu allen Teilnahmen an fremden Studien und auch zu allen eigenen Studien eingesehen werden. Es besteht die Möglichkeit
letztere zu korrigieren oder zu ergänzen.

![Main Pages Screenshot](https://github.com/ASE-Projekte-WS-2021/ase-ws-21-vp-manager/blob/improvement_readMe/wiki_Images/app_screenshots.png)

 Eine detaillierte Dokumentation des VP-Managers finden sie in der [Präsentation](https://docs.google.com/presentation/d/10WS_gH9wkGFBhsbHTYtQ16cwsczPvLDgANnSx5P_wNw/edit).
 

## Team

### Kai Aslan ([@Mutigo](https://github.com/Mutigo))
- Studien erstellen 
- Login/Registrierung
- Studien/Teilnahmestatus
- Warnungen und Hinweise (Pop-ups)
### Kilian Bogner ([@KilianBogner](https://github.com/KilianBogner))
- Code-Architektur
- Datenbank
- Navigation
- Studien bearbeiten
### Sebastion Hahn ([@Basti](https://github.com/basti7))
- Studien finden
- Deteilansicht der Studien
- Studien bearbeiten
- Warnungen und Hinweise (Pop-ups)
### Niklas Jahning ([@Tokimetakaari](https://github.com/Tokimetakaari))
- Mein VP-Status
- Meine Übersicht
- Studien/Teilnahmestatus
- Sortier- und Filterfunktionen

Anmerkung: Die genannten Bereiche wurden primär den jeweiligen Teammitgliedern zugewiesen. Bei Überschneidungen wurde paarweise gearbeitet.


## Bauen der App auf Basis des Repositorys

- Mit git: `git clone https://github.com/ASE-Projekte-WS-2021/ase-ws-21-vp-manager` und öffne den Ordner anschließend in Android Studio

  oder

- Mit Android Studio: Wähle "Get from VCS" und gib diesen Link ein: https://github.com/ASE-Projekte-WS-2021/ase-ws-21-vp-manager.git 

  anschießend

- Wähle "trust project" wenn es gefragt wird 
- Wähle "sync gradle files" für den Fall das es nicht automatishc startet 
- Wähle in Android Studio den Reiter "Build" > Build Bundle(s)/APK(s) > Build APK(s)
- Die APK ist dann hier zu finden: app\build\intermediates\apk\debug
