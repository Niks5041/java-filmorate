# java-filmorate

## Схема базы данных

![Схема базы данных](https://github.com/Niks5041/java-filmorate/blob/0eeb09947c37c898d938d116a77ebcc0465ae626/%D0%A1%D1%85%D0%B5%D0%BC%D0%B0%20%D0%B1%D0%B0%D0%B7%D1%8B%20%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85.png)

#### Примеры запросов:

1. **Получение списка всех пользователей:**
```
   SELECT * FROM User;
```
2. **Получение списка друзей:**
```
   SELECT * FROM Friends;
```
3. **Получение списка имен всех друзей конкретного пользователя:**
```
   SELECT u.name
   FROM Friends AS f
   JOIN User AS u ON u.id = f.friend_id
   WHERE f.user_id = x;
 ```

4. **Получение информации о фильме по его идентификатору:**
```
   SELECT *
   FROM Film
   WHERE film_id = x;
```

5. **Получение всех фильмов определённого жанра:**
```
   SELECT *
   FROM Film
   WHERE genre_id = x;
```

6. **Получение всех фильмов, выпущенных после определённой даты:**
```
   SELECT *
   FROM Film
   WHERE CAST(releaseDate AS DATE) < 'YYYY-MM-DD';
```

8. **Получение всех друзей пользователя с определённым статусом дружбы:**
```
    SELECT u.name
    FROM Friends AS f
    JOIN User AS u ON u.id = f.friend_id
    WHERE f.user_id = x
    AND friendship_status = 'TRUE';
```


 
   
    
