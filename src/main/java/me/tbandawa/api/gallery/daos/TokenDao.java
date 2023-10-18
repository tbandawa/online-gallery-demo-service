package me.tbandawa.api.gallery.daos;

import java.util.List;
import me.tbandawa.api.gallery.entities.UserToken;

public interface TokenDao {
	UserToken addToken(UserToken userToken);
	List<UserToken> getToken(long userId);
	int deleteToken(long userId);
}
