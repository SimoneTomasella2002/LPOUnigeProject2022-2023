package Progetto_Gruppo_33.parser.ast;

import Progetto_Gruppo_33.visitors.Visitor;

public interface AST {
	<T> T accept(Visitor<T> visitor);
}
