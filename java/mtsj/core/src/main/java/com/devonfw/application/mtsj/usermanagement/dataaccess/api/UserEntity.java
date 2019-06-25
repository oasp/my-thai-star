package com.devonfw.application.mtsj.usermanagement.dataaccess.api;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.usermanagement.common.api.User;

@Entity
@Table(name = "\"USER\"")
public class UserEntity extends ApplicationPersistenceEntity implements User {

  private String username;

  private String password;

  private String email;

  private UserRoleEntity userRole;

  private List<BookingEntity> bookings;

  private List<DishEntity> favourites;

  private static final long serialVersionUID = 1L;

  /**
   * @return username
   */
  @Override
  public String getUsername() {

    return this.username;
  }

  /**
   * @param username new value of {@link #getusername}.
   */
  @Override
  public void setUsername(String username) {

    this.username = username;
  }

  /**
   * @return password
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getpassword}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

  /**
   * @return email
   */
  @Override
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getemail}.
   */
  @Override
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return userRole
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idRole")
  public UserRoleEntity getUserRole() {

    return this.userRole;
  }

  /**
   * @param userRole new value of {@link #getuserRole}.
   */
  public void setUserRole(UserRoleEntity userRole) {

    this.userRole = userRole;
  }

  /**
   * @return favourites
   */
  @ManyToMany
  @JoinTable(name = "UserFavourite", joinColumns = {
  @javax.persistence.JoinColumn(name = "idUser") }, inverseJoinColumns = @javax.persistence.JoinColumn(name = "idDish"))
  public List<DishEntity> getFavourites() {

    return this.favourites;
  }

  /**
   * @param favourites new value of {@link #getfavourites}.
   */
  public void setFavourites(List<DishEntity> favourites) {

    this.favourites = favourites;
  }

  /**
   * @return bookings
   */
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  public List<BookingEntity> getBookings() {

    return this.bookings;
  }

  /**
   * @param bookings new value of {@link #getbookings}.
   */
  public void setBookings(List<BookingEntity> bookings) {

    this.bookings = bookings;
  }

  @Override
  @Transient
  public Long getUserRoleId() {

    if (this.userRole == null) {
      return null;
    }
    return this.userRole.getId();
  }

  @Override
  public void setUserRoleId(Long userRoleId) {

    if (userRoleId == null) {
      this.userRole = null;
    } else {
      UserRoleEntity userRoleEntity = new UserRoleEntity();
      userRoleEntity.setId(userRoleId);
      this.userRole = userRoleEntity;
    }
  }

}
