package odataservice.database.dao;

import com.mongodb.WriteResult;
import odataservice.database.collections.Sflight;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 *
 */
public interface SflightDAO extends DAO<Sflight, ObjectId> {

    Sflight getById(String id);

    Key<Sflight> save(Sflight sflight);

    WriteResult delete(Sflight sflight);

    boolean idTaken(String id);

    List<Sflight> getAllSflights();

    Sflight findFlightByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate);

    List<Sflight> findFlightsByCarrierId(String carrierCode);

    List<Sflight> findFlightsByCarrierIdAndConnectionId(String carrierId, String connectionId);
}
