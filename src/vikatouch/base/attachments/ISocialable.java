package vikatouch.base.attachments;

// Нужен для объединения разных объектов ВК, умеющих в социалку. Например, в ImagePreview. Пригодится короче.
public interface ISocialable {
	public boolean canSave(); // возвращать false например для УЖЕ своиих фото.
	public void save(); // сохранить себе
	public void like(boolean val); // 1 - поставить лайк, 0 - убрать.
	public void send(); // отправить в лс
	public void repost(); // репост. Всё кроме постов - при вызове ничего не делать.
	public void openComments(); // открыть экран комментариев
	//public void sendComment(Comment comment); // отправить коммент
}
