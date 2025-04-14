package Progetto_Gruppo_33.parser;

import Progetto_Gruppo_33.parser.ast.Prog;

public interface Parser extends AutoCloseable {

	Prog parseProg() throws ParserException;

}