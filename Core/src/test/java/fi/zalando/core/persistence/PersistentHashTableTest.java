package fi.zalando.core.persistence;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.zalando.core.BuildConfig;
import fi.zalando.core.module.BasePersistenceModule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PersistentHashTable}
 *
 * Created by jduran on 23/11/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, manifest =
        "src/main/AndroidManifest.xml")
public class PersistentHashTableTest {

    private PersistentHashTable persistentHashTable;

    @Before
    public void setup() {

        persistentHashTable = new BasePersistenceModule()
                .providePersistentHashTable(RuntimeEnvironment.application);
    }

    @Test
    public void testSavingBoolean() {

        final String key = "testSavingBoolean";

        // Test default values
        assertTrue(persistentHashTable.get(key, true));
        assertFalse(persistentHashTable.get(key, false));

        // Save a value with the key
        persistentHashTable.put(key, true);

        // Check if it is properly saved, providing as default a different value as saved one
        assertTrue(persistentHashTable.get(key, false));
        // Check that given a key with break lines at the end gives same result
        assertTrue(persistentHashTable.get(" " + key + "\n", false));
    }

    @Test
    public void testSavingString() {

        final String key = "testSavingString";

        // Test default values
        assertEquals(persistentHashTable.get(key, "shouldBeEqual"), "shouldBeEqual");

        // Save a value with the key
        persistentHashTable.put(key, "shouldBeEqual");

        // Check if it is properly saved, providing as default a different value as saved one
        assertEquals(persistentHashTable.get(key, "anotherString"), "shouldBeEqual");
        // Check that given a key with break lines at the end gives same result
        assertEquals(persistentHashTable.get(" " + key + "\n", "anotherString"), "shouldBeEqual");
        // Neither allow to save values at the end of the value string
        assertEquals(persistentHashTable.get(" " + key + "\n", "anotherString"), (" " +
                "shouldBeEqual\n").trim());
    }

    @Test
    public void testSavingLong() {

        final String key = "testSavingLong";

        // Test default values
        assertEquals(persistentHashTable.get(key, (long) 1).longValue(), (long) 1);

        // Save a value with the key
        persistentHashTable.put(key, (long) 1);

        // Check if it is properly saved, providing as default a different value as saved one
        assertEquals(persistentHashTable.get(key, (long) 2).longValue(), (long) 1);
        // Check that given a key with break lines at the end gives same result
        assertEquals(persistentHashTable.get(" " + key + "\n", (long) 2).longValue(), (long) 1);
    }

    @Test
    public void testSavingInteger() {

        final String key = "testSavingInteger";

        // Test default values
        assertEquals(persistentHashTable.get(key, 1).intValue(), 1);

        // Save a value with the key
        persistentHashTable.put(key, 1);

        // Check if it is properly saved, providing as default a different value as saved one
        assertEquals(persistentHashTable.get(key, 2).intValue(), 1);
        // Check that given a key with break lines at the end gives same result
        assertEquals(persistentHashTable.get(" " + key + "\n", 2).intValue(), 1);
    }

    @Test
    public void testSavingDate() {

        final String key = "testSavingDate";
        final Date dateToSave = new Date(System.currentTimeMillis());

        // Test default values
        assertEquals(persistentHashTable.get(key, dateToSave), dateToSave);

        // Save a value with the key
        persistentHashTable.put(key, dateToSave);

        // Check if it is properly saved, providing as default a different value as saved one
        assertEquals(persistentHashTable.get(key, new Date(0)), dateToSave);
        // Check that given a key with break lines at the end gives same result
        assertEquals(persistentHashTable.get(" " + key + "\n", new Date(0)), dateToSave);
    }

    @SuppressWarnings("all")
    @Test
    public void testSavingMap() {

        Map<String, Object> mapToSave = new HashMap<>();

        String booleanKey = "booleanKey";
        boolean booleanValue = true;
        String stringKey = "stringKey";
        String stringValue = "stringValue";
        String longKey = "longKey";
        long longValue = (long) 1;
        String integerKey = "integerKey";
        int integerValue = 2;
        String dateKey = "dateKey";
        Date dateValue = new Date();

        // Add values to the map
        mapToSave.put(booleanKey, booleanValue);
        mapToSave.put(stringKey, stringValue);
        mapToSave.put(longKey, longValue);
        mapToSave.put(integerKey, integerValue);
        mapToSave.put(dateKey, dateValue);

        // Test that values we are going to add do not exist
        assertEquals(persistentHashTable.get(booleanKey, !booleanValue), !booleanValue);
        assertEquals(persistentHashTable.get(stringKey, "anotherString"), "anotherString");
        assertEquals(persistentHashTable.get(longKey, Long.MAX_VALUE).longValue(), Long.MAX_VALUE);
        assertEquals(persistentHashTable.get(integerKey, Integer.MAX_VALUE).intValue(), Integer
                .MAX_VALUE);
        assertEquals(persistentHashTable.get(dateKey, new Date(0)), new Date(0));

        // Save the values
        persistentHashTable.put(mapToSave);

        // Check they are saved
        assertEquals(persistentHashTable.get(booleanKey, !booleanValue), booleanValue);
        assertEquals(persistentHashTable.get(stringKey, "anotherString"), stringValue);
        assertEquals(persistentHashTable.get(longKey, Long.MAX_VALUE).longValue(), longValue);
        assertEquals(persistentHashTable.get(integerKey, Integer.MAX_VALUE).intValue(),
                integerValue);
        assertEquals(persistentHashTable.get(dateKey, new Date(0)), dateValue);

        // Check that given a key with break lines at the end gives same result
        assertEquals(persistentHashTable.get(" " + stringKey + "\n", "anotherString"), stringValue);
        // Neither allow to save values at the end of the value string
        assertEquals(persistentHashTable.get(" " + stringKey + "\n", "anotherString"), (" "
                + stringValue + "\n").trim());
    }

    @Test
    public void testReset() {

        String stringKey = "stringKey";
        String stringValue = "stringValue";

        // add some value first
        persistentHashTable.put("stringKey", stringValue);

        // Check that it was saved
        assertEquals(persistentHashTable.get(stringKey, "anotherString"), stringValue);

        // Reset it
        persistentHashTable.reset();

        // Check if it the previously saved value does not exist anymore
        assertNotEquals(persistentHashTable.get(stringKey, "anotherString"), stringValue);
    }

    @Test
    public void testIsEmpty() {

        String stringKey = "stringKey";
        String stringValue = "stringValue";

        assertTrue(persistentHashTable.isEmpty());
        // add some value
        persistentHashTable.put(stringKey, stringValue);
        // Check that now it's not empty
        assertFalse(persistentHashTable.isEmpty());

        // switch storage
        persistentHashTable.switchStorage("anotherone!");
        assertTrue(persistentHashTable.isEmpty());
        // add some value
        persistentHashTable.put(stringKey, stringValue);
        // Check that now it's not empty
        assertFalse(persistentHashTable.isEmpty());
        // reset storage
        persistentHashTable.reset();
        // check is empty again
        assertTrue(persistentHashTable.isEmpty());
    }

    @Test
    public void testSwitchStorage() {

        final String key = "testSwitchStorage";
        final String anotherStorageName = "otherStorageName";

        // Save a value with the key in default value
        persistentHashTable.put(key, true);

        // Check if it is properly saved, providing as default a false value
        assertTrue(persistentHashTable.get(key, false));

        // switch storage to anotherStorageName
        persistentHashTable.switchStorage(anotherStorageName);

        // Check that previously stored value does not exist anymore
        assertFalse(persistentHashTable.get(key, false));
    }

}