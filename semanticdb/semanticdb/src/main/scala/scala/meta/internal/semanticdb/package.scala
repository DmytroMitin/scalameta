package scala.meta.internal

package object semanticdb {

  val NoType = Type.Empty
  val NoConstant = Constant.Empty
  val NoSignature = Signature.Empty

  implicit class XtensionSemanticdbSymbolInformation(info: SymbolInformation) {
    def has(
        prop: SymbolInformation.Property,
        prop2: SymbolInformation.Property,
        props: SymbolInformation.Property*): Boolean =
      has(prop) && has(prop2) && props.forall(has)
    def has(prop: SymbolInformation.Property): Boolean =
      (info.properties & prop.value) != 0
  }

  implicit class XtensionSemanticdbScope(scope: Scope) {
    def symbols: List[String] = {
      if (scope.symlinks.nonEmpty) scope.symlinks.toList
      else scope.hardlinks.map(_.symbol).toList
    }
    def infos: List[SymbolInformation] = {
      if (scope.symlinks.nonEmpty) {
        scope.symlinks.map(symbol => SymbolInformation(symbol = symbol)).toList
      } else {
        scope.hardlinks.toList
      }
    }
  }

  implicit class XtensionSemanticdbScopeOpt(scopeOpt: Option[Scope]) {
    def symbols: List[String] = scopeOpt.map(_.symbols).getOrElse(Nil)
    def infos: List[SymbolInformation] = scopeOpt.map(_.infos).getOrElse(Nil)
  }

  implicit class XtensionSemanticdbScopes(scopes: Seq[Scope]) {
    def symbols: List[List[String]] = scopes.map(_.symbols).toList
    def infos: List[List[SymbolInformation]] = scopes.map(_.infos).toList
  }

  implicit class XtensionSemanticdbType(tpe: Type) {
    def nonEmpty: Boolean = tpe.isDefined
  }

  implicit class XtensionSemanticdbSignature(sig: Signature) {
    def nonEmpty: Boolean = sig.isDefined
  }

  implicit class XtensionSemanticdbConstant(const: Constant) {
    def value: Option[Any] = {
      const match {
        case NoConstant => None
        case UnitConstant() => Some(())
        case BooleanConstant(value) => Some(value)
        case ByteConstant(value) => Some(value.toByte)
        case ShortConstant(value) => Some(value.toShort)
        case CharConstant(value) => Some(value.toChar)
        case IntConstant(value) => Some(value)
        case LongConstant(value) => Some(value)
        case FloatConstant(value) => Some(value)
        case DoubleConstant(value) => Some(value)
        case StringConstant(value) => Some(value)
        case NullConstant() => Some(null)
      }
    }
  }

  implicit class XtensionSemanticdbConstantCompanion(const: Constant.type) {
    def apply(value: Any): Constant = {
      value match {
        case () => UnitConstant()
        case value: Boolean => BooleanConstant(value)
        case value: Byte => ByteConstant(value.toInt)
        case value: Short => ShortConstant(value.toInt)
        case value: Char => CharConstant(value.toInt)
        case value: Int => IntConstant(value)
        case value: Long => LongConstant(value)
        case value: Float => FloatConstant(value)
        case value: Double => DoubleConstant(value)
        case value: String => StringConstant(value)
        case null => NullConstant()
        case _ => sys.error("unsupported value ${value.getClass} $value")
      }
    }
  }
}
