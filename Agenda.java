import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A node of the Agenda. Contains the
 * data of an Entry.
 */
class AgendaNode {
	Entry info;
	AgendaNode sig;

	AgendaNode(Entry p, AgendaNode siguiente) {
		info = p;
		sig = siguiente;
	}
}

public class Agenda implements AgendaInterface {
	private AgendaNode first;
	private int numEntries;

	public Agenda() {
		first = null;
		numEntries = 0;
	}

	/** STATIC ANALYSIS
	 * The function addEntry must add a new contact to the agenda. This function will
	 * receive the values to each field of the contact person. The new person will be stored into a
	 * list sorted by name and surname. If the agenda is empty, then the person added will be
	 * the first contact in the list. This function must return true if the
	 * contact has been stored successfully and false in the case of duplicate values of name and
	 * surname fields.
	 */
	public boolean addEntry(Entry p) {
		AgendaNode cur;
		boolean found = false;

		if (first != null) {
			cur = first;
			while (cur != null && !found) {
				if (cur.info.getName() == p.getName()) {		//si encuentra un contacto con el mismo nombre pero distinto apellido no lo inserta
					found = true;
				}

				cur = cur.sig;
			}
		}

		if (!found) {
			first = new AgendaNode(p, first);					//inserta el contacto el primero sin tener en cuenta el órden alfabético
			return true;
		} else
			return false;
	}

	/** STATIC ANALYSIS
	 * The function removeEntry must remove a contact from the agenda. This function will
	 * receive the name of the contact person that must be removed. If the agenda is empty the
	 * function does not have any impact in the list. If the agenda has only one contact and this
	 * contact is the one to be removed, the resulted list must be empty. This function must return
	 * true if the contact has been removed successfully and false in the opposite.
	 */
	public boolean removeEntry(String name) {
		if (first == null) {
			return false;
		}

		AgendaNode prev = first;
		while (prev.sig != null && prev.sig.info.getName() != name) {		//no comprueba el primer contacto de la agenda
			prev = prev.sig;
		}

		if (prev.sig == null) {												//si borra el único contacto de la agenda no devuelve una lista vacía
			return false;
		} else {
			prev.sig = prev.sig.sig;
			numEntries--;
			return true;
		}
	}

	/** STATIC ANALYSIS
	 * The function removeFirst must remove the first contact of the agenda. If the agenda is
	 * empty the function does not have any impact in the list. If the agenda has only one contact,
	 * this contact must be removed and the resulted list must be empty. This function must return
	 * a Entry object if the contact has been removed successfully and null in the opposite.
	 */
	public Entry removeFirst() {
		Entry p = null;

		if (first != null) {
			p = first.info;
			first = first.sig;
			numEntries--;
		}

		return p;
	}

	public int nEntries() {
		return numEntries;
	}

	public boolean isEmpty() {
		if (first == null) {
			return true;
		} else {
			return false;
		}
	}

	/** STATIC ANALYSIS
	 * The function saveAgenda must store the agenda in a text file named agendafile.txt.
	 * If the agenda is empty, the function does not create the file. The agenda must remain
	 * unchanged after the saving. The function returns true if the agenda was successfully
	 * saved and false otherwise.
	 */
	public boolean saveAgenda() throws IOException {
		AgendaNode cur = first;
		String line;
		boolean success = false;
		Parser p = new Parser();

		FileWriter fichero = new FileWriter("agendofile.txt");			//aunque la agenda esté vacía crea el fichero, el fichero tiene nombre incorrecto
		BufferedWriter bufferescritura = new BufferedWriter(fichero);
		PrintWriter output = new PrintWriter(bufferescritura);

		while (cur != null) {
			if (!success) {
				success = true;
			}
			p.insertEntry(cur.info);
			line = p.getLine();
			output.println(line);
		}

		return success;
	}

	/** STATIC ANALYSIS
	 * The function loadAgenda must load the agenda from the file agendafile.txt,
	 * adding all the entries in that file. If the agenda is not empty, all the
	 * pre-existing contacts are removed and the final agenda only contain
	 * the entries of the file. If the file agendafile.txt is empty or
	 * does not exists, the agenda remain unchanged.
	 * The function returns true is the file exists and false otherwise.
	 */
	public boolean loadAgenda() throws IOException {
		FileReader filein = new FileReader("agendafile.txt");
		BufferedReader bufferin = new BufferedReader(filein);

		Parser p = new Parser();
		String cad;
		if ((cad = bufferin.readLine()) == null) {		//en ningún momento abre el fichero por lo que controla la excepción y la función no se ejecuta
			bufferin.close();
			return false;
		}
		
		do {
			System.out.println(cad);
			p.insertLine(cad);
			Entry auxEntry = p.getEntry();
			if (auxEntry.hasData()) {
				addEntry(auxEntry);
			}
		} while ((cad = bufferin.readLine()) != null);
		filein.close();

		return true;
	}
}
