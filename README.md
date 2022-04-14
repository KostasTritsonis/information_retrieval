# ΑΝΑΚΤΗΣΗ ΠΛΗΡΟΦΟΡΙΑΣ

## ΟΜΑΔΑ
ΝΕΚΤΑΡΙΟΣ ΒΙΔΑΛΑΚΗΣ Α.Μ. 4033

ΚΩΝΣΤΑΝΤΙΝΟΣ ΤΡΙΤΣΩΝΗΣ Α.Μ. 4185

## Περιγραφή του dataset

Για την υλοποίηση της παρούσας εργασίας χρησιμοποιήθηκε μια εκ των ετοίμων συλλογών που παρουσιάστηκαν στο μάθημα. Πιο συγκεκριμένα χρησιμοποιήσαμε την συλλογή tmdb top 10000 popular movies dataset(https://www.kaggle.com/datasets/sankha1998/tmdb-top-10000-popular-movies-dataset).

#### Το format του dataset είναι τύπου csv και αποτελείται από 6 πεδία:

•	Το 1ο πεδίο αφορά την αρίθμηση των ταινιών.

•	Το 2ο πεδίο αφορά τον τίτλο της εκάστοτε ταινίας. 

•	Το 3ο πεδίο αφορά μία σύντομη περιγραφή της ταινίας. 

•	Το 4ο πεδίο αφορά την γλώσσα ομιλίας.

•	Το 5ο πεδίο αφορά το συνολικό αριθμών αξιολογήσεων της ταινίας.

•	Το 6ο πεδίο αφορά το μέσο όρο των αξιολογήσεων.

## Λειτουργικότητα Συστήματος
Στόχος αυτής της εργασίας είναι να υλοποιήσουμε ένα σύστημα αναζήτησης ταινιών ή κριτικών για ταινίες. Η λειτουργικότητα του παρών συστήματος αναλύεται συνοπτικά στα παρακάτω πεδία:

 #### •	Ανάλυση κειμένου και κατασκευή ευρετηρίου.

Προ επεξεργασία των δεδομένων και δημιουργία του εγγράφου και των ευρετηρίων για την αναζήτηση. Κατά την διάρκεια της αναζήτησης μπορούν να χρησιμοποιηθούν διάφορες λειτουργίες , όπως η διόρθωση τυπογραφικών λαθών, ή η επέκταση ακρωνύμων με σκοπό τα βέλτιστα αποτελέσματα.

 #### •	Αναζήτηση λέξεων.

Ο χρήστης έχει την δυνατότητα να αναζητήσει μέσα στο dataset χρησιμοποιώντας λέξεις κλειδιά.

 #### •	Αναζήτηση σε πεδίο.

Ο χρήστης έχει την επιλογή να διαλέξει σε ποια πεδία του αρχείου να πραγματοποιηθεί η αναζήτηση(πχ. Τίτλος , πλοκή).

 #### •	Ιστορικό αναζητήσεων.

Το σύστημα διατηρεί ένα ιστορικό αναζητήσεων ώστε να μπορεί να προτείνει εναλλακτικά ερωτήματα στον χρήστη.

 #### •	Παρουσίαση των Αποτελεσμάτων.

Το σύστημα παρουσιάζει τα αποτελέσματα σε διάταξη με βάση τη συνάφεια τους με το ερώτημα.


## Προ Επεξεργασία

Η προ επεξεργασία των δεδομένων γίνεται ως εξής. Αρχικά διαβάζουμε το αρχείο μέσω ενός csvReader που δημιουργήσαμε και αποθηκεύουμε τα δεδομένα σε ένα Αrraylist. Χρησιμοποιούμε  StandardAnalyzer για την ανάλυση μας επειδή θεωρείται μία πιο εκλεπτυσμένη μορφή των υπόλοιπων analyzers, ενώ παράλληλα δίνουμε τα δεδομένα στον IndexWriter.
Η αποθήκευση του index γίνεται σε ένα ξεχωριστό αρχείο στον δίσκο μέσω του FSDirectory.open.
Στην συνέχεια δημιουργούμε το Document και προσθέτουμε τα πεδία που θέλουμε να χρησιμοποιήσουμε(τίτλο, πλοκή, μέσο όρο αξιολογήσεων).Τα πεδία που θέλουμε να γίνουν tokenized είναι ο τίτλος και η πλοκή γι’ αυτό τον λόγο χρησιμοποιούμε new TextField ενώ για την συνολική αξιολόγηση που δεν χρειάζεται tokenized κάνουμε new StringField. Επίσης δίνουμε setOpenMode στον IndexWriterConfig  Create and Update ώστε να δημιουργεί και να ανανεώνει το ευρετήριο. Μονάδα του εγγράφου αποτελεί ο τίτλος ή η πλοκή. Δημιουργούμε ευρετήρια για τα πεδία τίτλος και πλοκή. 

## Αναζήτηση

Αρχικά λαμβάνουμε την λέξη κλειδί για την αναζήτηση και διαβάζουμε το directory μέσω του DirectoryReader. Δημιουργούμε έναν QueryParser και κάνουμε parse την λέξη κλειδί. Έπειτα κάνουμε search και αποθηκεύουμε τα αποτελέσματα σε ένα TopDocs  και εκτελούμε την εντολή 
scoreDocs για να πάρουμε τα hits τα οποία διατρέχουμε και δίνουμε στο σύστημα για να τα εμφανίσει κατάλληλα στον χρήστη.

## Παρουσίαση Αποτελεσμάτων

Το σύστημα παρουσιάζει τα αποτελέσματα σε διάταξη με βάση τη συνάφεια τους με το ερώτημα. Τα αποτελέσματα θα εμφανίζονται στον χρήστη ανά 10 με την δυνατότητα αλλαγή σελίδας μπρος/πίσω και η λέξη κλειδί θα εμφανίζεται τονισμένη. Επίσης θα μπορεί να γίνει αναδιάταξη των αποτελεσμάτων με βάση τον τίτλο ή την συνολική αξιολόγηση.
