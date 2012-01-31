package sessionj.types.sesstypes;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.*;
import polyglot.types.reflect.ClassFileLoader;
import sessionj.Version;
import sessionj.types.SJTypeSystem;
import sessionj.types.SJTypeSystem_c;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// Ray: I replaced all occurrences of SJSessionType_c with SJSessionType (following the changes to the implementation of SJSetType_c).
public class SetTypeTest {
    private SJSetType set;
    private List<SJSessionType> members;
    private SJTypeSystem ts;
    private SJSendType_c sendBool;
    private SJSessionType sendString;
    private SJSessionType receiveString;
    private SJSessionType sendObject;
    private SJSessionType receiveObject;
    private SJSessionType sendInt;

    @BeforeTest
    public void createSet() throws SemanticException {
        ts = new SJTypeSystem_c();
        String classpath = System.getProperty("java.class.path");
        ExtensionInfo ext = new sessionj.ExtensionInfo();
        TopLevelResolver resolver = new LoadedClassResolver
            (ts, classpath, new ClassFileLoader(ext), new Version(), true);
        ts.initialize(resolver, ext);
        sendBool = sendType(PrimitiveType.BOOLEAN);
        sendString = new SJSendType_c(ts, ts.String());
        sendObject = new SJSendType_c(ts, ts.Object());
        sendInt = sendType(PrimitiveType.INT);
        receiveObject = new SJReceiveType_c(ts, ts.Object());
        receiveString = new SJReceiveType_c(ts, ts.String());
        members = new LinkedList<SJSessionType>() {{
            add(sendBool);
            add(sendInt);
        }};
        set = new SJSetType_c(ts, members);
    }

    private SJSendType_c sendType(PrimitiveType.Kind primitive) throws SemanticException {
        return new SJSendType_c(ts, new PrimitiveType_c(ts, primitive));
    }

    @Test
    public void subtypeOfSameSetType() throws SemanticException {
        Type otherSet = new SJSetType_c(ts, members);

        assert set.isSubtype(otherSet);
        assert otherSet.isSubtype(set);
    }

    @Test
    public void subtypeOfMemberOfSet() {
        assert sendBool.isSubtype(set);
        assert !set.isSubtype(sendBool);
    }

    @Test
    public void notSubtypeNotMemberOfSet() throws SemanticException {
        Type sendFloat = sendType(PrimitiveType.FLOAT);
        assert !sendFloat.isSubtype(set);
        assert !set.isSubtype(sendFloat);
    }

    @Test
    public void subtypeSmallerSetType() {
        List<SJSessionType> smaller = new LinkedList<SJSessionType>();
        smaller.add(sendBool);
        Type smallerSet = new SJSetType_c(ts, smaller);

        assert smallerSet.isSubtype(set);
        assert !set.isSubtype(smallerSet);
    }

    @Test 
    public void setElementsAreSubtypes() {
        List<SJSessionType> listSubtypes = new LinkedList<SJSessionType>() {{
            add(sendObject);
            add(receiveString);
        }};
        List<SJSessionType> listSupertypes = new LinkedList<SJSessionType>() {{
            add(sendString);
            add(receiveObject);
            add(sendBool);
        }};
        Type setWithSubtypes = new SJSetType_c(ts, listSubtypes);
        Type setWithSupertypes = new SJSetType_c(ts, listSupertypes);
        assert !setWithSupertypes.isSubtype(setWithSubtypes);
        assert setWithSubtypes.isSubtype(setWithSupertypes);
    }

    //@Test
    public void subsumeSetAndMemberOfSet() throws SemanticException {
        SJSessionType result = set.subsume(sendBool);
        assert result instanceof SJSetType;
        // a singleton with just sendBool in it
        assert result.isSubtype(sendBool);
        assert sendBool.isSubtype(result);
    }

    //@Test
    public void subsumeSetAndSubtypeOfMemberOfSet() throws SemanticException {
        SJSessionType setWithSupertype = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendString);
            add(sendBool);
        }});

        SJSessionType result = setWithSupertype.subsume(sendObject);
        assert result instanceof SJSetType;
        assert result.isSubtype(sendObject);
        assert sendObject.isSubtype(result);
    }

    //@Test
    public void subsumeFoo() throws SemanticException {
        SJSessionType set1 = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendString);
            add(sendObject);
        }});

        SJSessionType set2 = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendString);
        }});
        SJSessionType result = set1.subsume(set2);
        assert result.isSubtype(sendString);
        assert sendString.isSubtype(result);
    }

    //@Test(expectedExceptions = SemanticException.class)
    public void unsuccessfulSubsuption() {
        
    }

    @Test
    public void sameSetShouldContainsAllAndOnly() {
        assert set.containsAllAndOnly(new HashSet<SJSessionType>(members));
    }

    @Test
    public void biggerSetShouldntContainsAllAndOnly() {
        SJSetType bigger = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendBool);
            add(sendInt);
            add(sendString);
        }});
        assert !bigger.containsAllAndOnly(new LinkedList<SJSessionType>(members));
    }

    @Test
    public void setsSameMembersShouldBeEqual() {
        Object same = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendInt); add(sendBool);
            // same members but different order - should make no difference        
        }});
        assert same.equals(set);
        assert set.equals(same);
    }

    @Test
    public void setAndOtherTypeShouldntBeEqual() {
        assert !set.equals(sendInt);
        assert !sendInt.equals(set);
    }

    @Test
    public void setsWithDifferentMembersShouldntBeEqual() {
        Object diffMembers = new SJSetType_c(ts, new LinkedList<SJSessionType>() {{
            add(sendBool); add(sendInt); add(sendObject);
        }});
        assert !diffMembers.equals(set);
        assert !set.equals(diffMembers);
    }
}
