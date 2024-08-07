package uz.pdp.service.room;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.pdp.daos.roomsDao.RoomDao;
import uz.pdp.daos.userDao.UserDao;
import uz.pdp.model.Rooms;
import uz.pdp.service.BaseService;

import java.util.List;

@Service
public class RoomService implements BaseService<Rooms> {

    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }


    @Override
    public void save(Rooms entity) {
        roomDao.save(entity);
    }

    @Override
    public void update(Rooms entity) {
        roomDao.update(entity);
    }

    @Override
    public void delete(int id) {
        roomDao.delete(id);
    }

    @Override
    public Rooms getById(int id) {
        return roomDao.getById(id);
    }

    @Override
    public List<Rooms> getAll() {
        return roomDao.getAll();
    }
}
